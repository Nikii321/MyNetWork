package com.example.MyNetWork.controller;

import com.example.MyNetWork.entity.User;
import com.example.MyNetWork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FriendsController {
    @Autowired
    private UserService userService;

    @GetMapping("/friend")
    public String showFriends(Model model){
        String str = "";
        model.addAttribute("Find User", str);
        model.addAttribute("Users",userService.getPopularUsers());
        return "friendsList";
    }
    @PostMapping("/friend")
    public String findUserByUsername(@RequestParam(required = true, defaultValue = "") String name, Model model){
        List<User> users = userService.findUsersListByName(name);
        if(users.isEmpty()||users == null){
            model.addAttribute("ERROR", "Not found");
        }
        model.addAttribute("Users", users);
        return "friendsList";
    }
}
