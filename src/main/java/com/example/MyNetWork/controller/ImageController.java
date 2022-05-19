package com.example.MyNetWork.controller;

import com.example.MyNetWork.service.ImageService;
import com.example.MyNetWork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class ImageController {
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;

    @GetMapping("/fileUpload")
    public String showUploaderImage(){
        return "fileLouder";
    }



    @PostMapping("/fileUpload")
    public String upload(@ModelAttribute("File") MultipartFile file, Model model){
        String localPath="/Users/nikolajvereschagin/Desktop/MyNetWork/src/main/webapp/resources/image";
        String warning="";
        if(imageService.upload(file, localPath, userService.getCurrentUsername())){
            warning="Download completed successfully";

        }else{
            warning="Download failed";
            model.addAttribute("Exeption");
            return "fileLouder";
        }
        model.addAttribute("Name",userService.getCurrentUsername());


        return "fileLouder";
    }


}