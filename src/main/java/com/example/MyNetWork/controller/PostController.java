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

        model.addAttribute("NewPost", "");
        model.addAttribute("I", userService.getCurrentUsername());


        return "post";
    }
    @PostMapping("/post")
    public String changeUser(@RequestParam(required = true, defaultValue = "") String text,
                             @ModelAttribute("File") MultipartFile file) {
        Post post = new Post();
        if(file != null) {
            post.setPath(imageService.upload(file));
        }
        post.setText(text);

        User user = userService.getCurrentUser();

        post.setAuthorId(user.getId());
        post.setAuthorName(user.getUsername());
        messageSender.send(post);

        System.out.println(post);
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