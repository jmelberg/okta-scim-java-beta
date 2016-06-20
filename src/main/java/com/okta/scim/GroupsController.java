package com.okta.scim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 *  URL route example.com/scim/v2/Groups
 */


@Controller
@RequestMapping("/scim/v2/Groups")
public class GroupsController {
    Database db;

    @Autowired
    public GroupsController(Database db) {
        this.db = db;
    }
    /**
     *  Returns default {@link ListResponse} object
     *
     *  @return JSON {@link Map} {@link ListResponse}
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Map groupsGet() {
        ListResponse groups = new ListResponse();
        return groups.toScimResource();
    }
}
