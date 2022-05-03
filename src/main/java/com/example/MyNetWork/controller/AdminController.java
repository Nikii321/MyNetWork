package com.example.MyNetWork.controller;

import com.example.MyNetWork.service.AdminService;
import com.example.MyNetWork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/admin")
    public String userList( Model model) {
        model.addAttribute("allUsers", adminService.allUsersForAdmin());
        model.addAttribute("allBaned",adminService.allBanForAdmin());

        return "admin";
    }

    @PostMapping("/admin")
    public String  deleteUser(@RequestParam(required = true, defaultValue = "" ) Long userId,
                              @RequestParam(required = true, defaultValue = "" ) String action)
    {
        if (action.equals("delete")){
            adminService.banUser(userId);
        }
        if (action.equals("Unban")){
            adminService.unbanUser(userId);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/gt/{userId}")
    public String  gtUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", adminService.usergtList(userId));
        return "admin";
    }
}