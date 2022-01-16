package com.urlshorter.site.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

    @RequestMapping("/admin-lk")
    String adminAccount(){

        return "admin-lk";
    }

    @RequestMapping("/admin-lk/settings")
    String settingsPage(Model model, Authentication auth){
        model.addAttribute("userEmail", auth.getName());

        return "settings";
    }
}
