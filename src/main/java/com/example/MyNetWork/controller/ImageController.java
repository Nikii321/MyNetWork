package com.example.MyNetWork.controller;

import com.example.MyNetWork.entity.UsDetails;
import com.example.MyNetWork.entity.User;
import com.example.MyNetWork.service.ImageService;
import com.example.MyNetWork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
public class ImageController {
    @Autowired
    ImageService imageService;
    @Autowired
    UserService userService;

    @GetMapping("/fileUpload")
    public String showUploaderImage(){
        return "fileLouder";
    }



    @PostMapping("/fileUpload")
    public String upload(@ModelAttribute("File") @Valid MultipartFile file, Model model){
        String localPath="/Users/nikolajvereschagin/Desktop/RusFaceApp/src/main/webapp/resources/image";

        String warning="";
        if(imageService.upload(file, localPath, userService.getCurrentUsername())){
            warning="Download completed successfully";

        }else{
            warning="Download failed";
        }
        model.addAttribute("Name",userService.getCurrentUsername());

        System.out.println(warning);
        return "fileLouder";
    }


}