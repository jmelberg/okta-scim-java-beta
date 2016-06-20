package com.okta.scim;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Returns an array of SCIM resources into a Query Resource
 */
public class ListResponse {
    private List<User> list;
    private int startIndex;
    private int count;
    private int totalResults;

    ListResponse(){
        this.list = new ArrayList<>();
        this.startIndex = 1;
        this.count = 0;
        this.totalResults = 0;
    }
    ListResponse(List<User> list, Optional<Integer> startIndex,
                 Optional<Integer> count, Optional<Integer> totalResults){
        this.list = list;

        // startIndex.orElse checks for optional values
        this.startIndex = startIndex.orElse(1);
        this.count = count.orElse(0);
        this.totalResults = totalResults.orElse(0);
    }

    /**
     * @return JSON {@link Map} of {@link ListResponse} object
     */
    Map<String, Object> toScimResource(){
        Map<String, Object> returnValue = new HashMap<>();

        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:api:messages:2.0:ListResponse");
        returnValue.put("schemas", schemas);
        returnValue.put("totalResults", this.totalResults);
        returnValue.put("startIndex", this.startIndex);

        List<Map> resources = this.list.stream().map(User::toScimResource).collect(Collectors.toList());

        if(this.count != 0) {
            returnValue.put("itemsPerPage", this.count);
        }
        returnValue.put("Resources", resources);

        return returnValue;
    }
}
