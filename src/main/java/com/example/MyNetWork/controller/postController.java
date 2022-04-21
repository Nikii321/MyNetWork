package com.example.MyNetWork.controller;

import com.example.MyNetWork.entity.User;
import com.example.MyNetWork.service.KafkaMessageSender;
import com.example.MyNetWork.service.UserService;
import com.example.postapi.model.Post;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class postController {
    @Autowired
    KafkaMessageSender messageSender;
    @Autowired
    UserService userService;

    @GetMapping("/post")
    public String showPost( Model model) {


        model.addAttribute("NewPost",new Post());

        return "post";
    }
    @PostMapping("/post")
    public String changeUser( @ModelAttribute("NewPost") Post post) {
            User user = userService.getCurrentUser();
            post.setFullName(user.getUsDetails().getNameUser());
            post.setAuthorId(user.getId());
            post.setAuthorName(user.getUsername());
            messageSender.send(post);

        return "redirect:/page/"+user.getUsername();
    }


    @GetMapping("/news")
    public String showNews( Model model) throws ExecutionException, InterruptedException {
        userService.sendKafkaListId();
        List<Post> posts = userService.getNews();

        System.out.println(posts);
        model.addAttribute("NewPost",posts);

        return "news";
    }

}
