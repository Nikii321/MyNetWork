package com.example.MyNetWork.controller;


import com.example.MyNetWork.entity.User;
import com.example.MyNetWork.service.EmailService;
import com.example.MyNetWork.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;



    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {

        System.out.println(userForm);
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }
        if (!userService.saveUser(userForm)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }

        return "redirect:/";
    }
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){


        boolean isActivation =userService.activateUser(code);

        if(isActivation){
            model.addAttribute("massage","User successfully activated ");
        }
        else{
            model.addAttribute("massage","Activation code not found");
        }



        return "activate";
    }
    @GetMapping("/temp")
    public String temp(){
        return "temp";
    }
}