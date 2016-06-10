package com.okta.scim;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name="users")
public class User {
    @Column(length=36)
    @Id
    public String id;

    @Column(columnDefinition="boolean default false")
    public Boolean active = false;

    @Column(unique=true, nullable=false, length=250)
    public String userName;

    @Column(length=250)
    public String familyName;

    @Column(length=250)
    public String middleName;

    @Column(length=250)
    public String givenName;

    // Default Constructor
    User() {}

    // Creates new User from JSON
    User(Map<String, Object> resource){
        this.update(resource);
    }

    void update(Map<String, Object> resource) {
        /*
          Updates User object from JSON

          Params:
                  JSON
        */
        try{
            Map<String, Object> names = (Map<String, Object>)resource.get("name");
            for(String subName : names.keySet()){
                switch (subName) {
                    case "givenName":
                        this.givenName = names.get(subName).toString();
                        break;
                    case "familyName":
                        this.familyName = names.get(subName).toString();
                        break;
                    case "middleName":
                        this.middleName = names.get(subName).toString();
                        break;
                    default:
                        break;
                }
            }
          this.userName = resource.get("userName").toString();
          this.active = (Boolean)resource.get("active");
        } catch(Exception e) {
             System.out.println(e);
        }
    }

    Map toScimResource(){
        /*
          Formats JSON map response with User attributes.

          Returns:
                  JSON map of User
        */

        Map<String, Object> returnValue = new HashMap<>();
        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:schemas:core:2.0:User");
        returnValue.put("schemas", schemas);
        returnValue.put("id", this.id);
        returnValue.put("active", this.active);
        returnValue.put("userName", this.userName);

        // Get names
        Map<String, Object> names = new HashMap<>();
        names.put("familyName", this.familyName);
        names.put("givenName", this.givenName);
        names.put("middleName", this.middleName);
        returnValue.put("name", names);

        // Get meta information
        Map<String, Object> meta = new HashMap<>();
        meta.put("resourceType", "User");
        meta.put("meta", ("/scim/v2/Users/" + this.userName));
        returnValue.put("meta", meta);

        return returnValue;
    }
}
