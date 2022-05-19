package com.example.MyNetWork.controller;


import com.example.MyNetWork.entity.User;
import com.example.MyNetWork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SubscriptionsController {
    @Autowired
    private UserService userService;

    @GetMapping("/subscriptions/{username}")
    public String showSubscription(@PathVariable String username, Model model){
        User user = userService.findUserByUsername(username);
        model.addAttribute("Subscriptions",user.getSubscriptions());
        model.addAttribute("I", userService.findUserByUsername(userService.getCurrentUsername()));
        return "SubcriptionsShow";
    }
    @GetMapping("/subscribers/{username}")
    public String showSubscriber(@PathVariable String username, Model model){
        User user = userService.findUserByUsername(username);
        model.addAttribute("Subscriber",user.getSubscribers());
        model.addAttribute("I", userService.findUserByUsername(userService.getCurrentUsername()));
        return "SubscriberShow";
    }
}