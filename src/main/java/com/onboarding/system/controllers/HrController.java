package com.onboarding.system.controllers;

import com.onboarding.system.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HrController {

    private final UserService userService;

    public HrController(UserService userService) {
        this.userService = userService;
    }

    // localhost:8080/hr/dashboard
    @GetMapping("/hr/dashboard")
    public String showHrDashboard(Model model) {

        model.addAttribute("users", userService.getAllUsers());

        return "hr-dashboard";
    }

    @org.springframework.web.bind.annotation.GetMapping("/hr/users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new com.onboarding.system.models.User());
        return "add-user";
    }

    @org.springframework.web.bind.annotation.PostMapping("/hr/users/add")
    public String saveUser(@org.springframework.web.bind.annotation.ModelAttribute("user") com.onboarding.system.models.User user) {
        user.setEnabled(true);
        userService.saveUser(user);

        return "redirect:/hr/dashboard";
    }

}