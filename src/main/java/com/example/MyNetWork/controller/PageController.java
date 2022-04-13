package com.example.MyNetWork.controller;

import com.example.MyNetWork.entity.UsDetails;
import com.example.MyNetWork.entity.User;
import com.example.MyNetWork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class PageController {
    @Autowired
    UserService userService;

    @GetMapping("/page/{username}")
    public String showPage(@PathVariable("username") String username, Model model){

        model.addAttribute("UserDetails", userService.findUserByUsername(username).getUsDetails());
        model.addAttribute("I",(userService.getCurrentUsername()));
        model.addAttribute("SubscribeButton", userService.isSubscribe(userService.getCurrentUsername(),username));
        return "PageUser";
    }
    @PostMapping("/page/{username}")
    public String subscribe(@PathVariable("username") String username, Model model){
        userService.subscriptionOrUnsubscription(userService.getCurrentUsername(),username);
        model.addAttribute("SubscribeButton", userService.isSubscribe(userService.getCurrentUsername(),username));

        return showPage(username,model);
    }




}