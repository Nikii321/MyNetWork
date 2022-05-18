package com.example.MyNetWork.controller;

import com.example.MyNetWork.entity.User;
import com.example.MyNetWork.service.DetailsService;
import com.example.MyNetWork.service.MessageSender;
import com.example.MyNetWork.service.UserService;
import com.example.detailsapi.model.Details;
import com.example.postapi.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class PageController {
    @Autowired
    UserService userService;
    @Autowired
    DetailsService detailsService;
    @Autowired
    MessageSender kafkaMessageSender;


    @GetMapping("/page/{username}")
    public String showPage(@PathVariable("username") String username, Model model){
        kafkaMessageSender.send(userService.findUserByUsername(username).getId());
        User user= userService.findUserByUsername(username);

        Details details= detailsService.getDetails(user.getId());

        model.addAttribute("UserDetails", details);
        model.addAttribute("SubscribersCount", user.getSubscribers().size());
        model.addAttribute("SubscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("I", userService.getCurrentUsername());
        model.addAttribute("SubscribeButton", userService.isSubscribe(userService.getCurrentUser().getUsername(),username));
        return "PageUser";
    }
    @PostMapping("/page/{username}")
    public String subscribe(@PathVariable("username") String username, Model model){
        userService.subscriptionOrUnsubscription(userService.getCurrentUsername(),username);
        kafkaMessageSender.send(userService.findUserByUsername(username).getId());
        User user= userService.findUserByUsername(username);
        Details details= detailsService.getDetails(user.getId());
        model.addAttribute("SubscribeButton", userService.isSubscribe(userService.getCurrentUsername(),username));
        model.addAttribute("UserDetails", details);
        model.addAttribute("SubscribersCount", user.getSubscribers().size());
        model.addAttribute("SubscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("I", userService.getCurrentUsername());

        return "PageUser";
    }




}