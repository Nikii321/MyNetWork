package com.example.MyNetWork.controller;

import com.example.MyNetWork.entity.User;
import com.example.MyNetWork.service.ImageService;
import com.example.MyNetWork.service.KafkaMessageSender;
import com.example.MyNetWork.service.PostService;
import com.example.MyNetWork.service.UserService;
import com.example.detailsapi.model.Details;
import com.example.postapi.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class PostController {
    @Autowired
    KafkaMessageSender messageSender;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    ImageService imageService;

    @GetMapping("/post")
    public String showPost( Model model) {

        messageSender.send(5L);
        model.addAttribute("NewPost",new Post());

        return "post";
    }
    @PostMapping("/post")
    public String changeUser(@ModelAttribute("NewPost") Post post,
                             @ModelAttribute("File") MultipartFile file,
                             @RequestParam(required = true, defaultValue = "") String action) {
        if(action.equals("addFile")) {
            post.setPath(imageService.upload(file));
            return "post";
        }


        User user = userService.getCurrentUser();
        post.setFullName(user.getUsDetails().getNameUser());
        post.setAuthorId(user.getId());
        post.setAuthorName(user.getUsername());
        messageSender.send(post);



        return "redirect:/page/"+user.getUsername();
    }


    @GetMapping("/news")
    public String showNews( Model model) throws ExecutionException, InterruptedException {
        postService.sendKafkaListId();
        List<Post> posts = postService.getNews();
        model.addAttribute("NewPost",posts);

        return "news";
    }

}