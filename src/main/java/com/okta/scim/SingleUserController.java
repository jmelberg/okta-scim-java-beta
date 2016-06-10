package com.okta.scim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.ArrayList;

/*
    SingleUsersController - URL route example.com/scim/v2/Users/{id}

    GET:    Returns user attributes given unique identifier

    PUT:    Updates resource

    PATCH:  Updates resource
*/

@Controller
@RequestMapping("/scim/v2/Users/{id}")
public class SingleUserController {
    Database db;

    @Autowired
    public SingleUserController(Database db) {
        this.db = db;
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Map singeUserGet(@PathVariable String id,  HttpServletResponse response) {
        /*
          Queries database for user with identifier
          Updates response with code '404' if unable to find

          Params:
                  String id - ID of user for query
          Returns:
                  scimError (See scimError doc)

        */
        try {
            User user = db.findById(id).get(0);
            return user.toScimResource();

        } catch (Exception e) {
            response.setStatus(404);
            return scimError("User not found", Optional.of(404));
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody Map singleUserPut(@RequestBody Map<String, Object> payload,
                                           @PathVariable String id) {
        /*
            Updates user attributes

            Params:
                    JSON map - payload
                    String id - id of user for update
            Returns:
                    renderJson (See renderJson doc)
        */

        User user = db.findById(id).get(0);
        user.update(payload);
        return user.toScimResource();
    }

    @RequestMapping(method = RequestMethod.PATCH)
    public @ResponseBody Map singleUserPatch(@RequestBody Map<String, Object> payload,
                                             @PathVariable String id) {
        /*
            Updates user attributes

            Params:
                    JSON map - payload
                    String id - id of user for update
            Returns:
                    scimError (See scimError doc)
                    renderJson (See renderJson doc)
        */

        List schema = (List)payload.get("schemas");
        List<Map> operations = (List)payload.get("Operations");

        if(schema == null){
            return scimError("Payload must contain schema attribute.", Optional.of(400));
        }
        if(operations == null){
            return scimError("Payload must contain operations attribute.", Optional.of(400));
        }

        //Verify schema
        String schemaPatchOp = "urn:ietf:params:scim:api:messages:2.0:PatchOp";
        if (!schema.contains(schemaPatchOp)){
            return scimError("The 'schemas' type in this request is not supported.", Optional.of(501));
        }

        //Find user for update
        User user = db.findById(id).get(0);

        for(Map map : operations){
            if(map.get("op")==null && !map.get("op").equals("replace")){
                continue;
            }
            Map<String, Object> value = (Map)map.get("value");

            // Use Java reflection to find and set User attribute
            if(value != null) {
                for (Map.Entry key : value.entrySet()) {
                    try {
                        Field field = user.getClass().getDeclaredField(key.getKey().toString());
                        field.set(user, key.getValue());
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        // Error - Do not update field
                    }
                }
            }
        }
        return user.toScimResource();
    }

    public Map scimError(String message, Optional<Integer> status_code){
        /*
            Output custom error message with response code

            Params:
                    String message - text to display
                    int status_code - server error code
            Returns
                    JSON dictionary
        */

        Map<String, Object> returnValue = new HashMap<>();
        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:api:messages:2.0:Error");
        returnValue.put("schemas", schemas);
        returnValue.put("detail", message);

        // Set default to 500
        returnValue.put("status", status_code.orElse(500));
        return returnValue;
    }
}
