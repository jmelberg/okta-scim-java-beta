package com.okta.scim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Web view URL route "/" to display users
 */
@Controller
@RequestMapping("/")
public class HomeController {

    private Database db;

    @Autowired
    public HomeController(Database db) {
        this.db = db;
    }

    /**
     * Outputs active users to web view
     *
     * @param model UI Model
     * @return HTML page to render by name
     */
    @RequestMapping(method = RequestMethod.GET)
    public String home(ModelMap model) {
        List<User> users = db.findAll();
        users.stream().filter(user -> !user.active).forEach(users::remove);
        model.addAttribute("users", users);
        return "home";
    }
}
