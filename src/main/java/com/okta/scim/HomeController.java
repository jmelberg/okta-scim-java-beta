package com.okta.scim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@Controller
@RequestMapping("/")
public class HomeController {

    private Database db;

    @Autowired
    public HomeController(Database db) {
        this.db = db;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(ModelMap model) {
        /*
            Outputs active users to web view
        */
        List<User> users = db.findAll();
        users.stream().filter(user -> !user.active).forEach(users::remove);
        model.addAttribute("users", users);
        return "home";
    }
}
