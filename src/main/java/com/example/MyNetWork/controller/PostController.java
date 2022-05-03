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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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
    public String addPost(@RequestParam(required = true, defaultValue = "") String text,
                             @ModelAttribute("File") MultipartFile file) {
        Post post = new Post();
        if(file != null) {
            post.setPath(imageService.upload(file));
        }
        post.setText(text);

        User user = userService.getCurrentUser();

        post.setAuthorId(user.getId());
        post.setAuthorName(user.getUsername());
        postService.sendNewPost(post);


        return "redirect:/page/"+user.getUsername();
    }



    @GetMapping("/news")
    public String showNews( Model model) throws ExecutionException, InterruptedException {
        postService.sendKafkaListId();
        List<Post> posts = postService.getNews();
        model.addAttribute("NewPost",posts);


        return "news";
    }
    @GetMapping("/post/{username}")
    public String AuthorsPost(@PathVariable("username") String username, Model model){
        Long id = userService.findUserByUsername(username).getId();
        messageSender.sendGetAuthorPosts(userService.findUserByUsername(username).getId());
        List<Post> posts = postService.getAuthorPost(username);
        model.addAttribute("NewPost",posts);
        model.addAttribute("I",userService.getCurrentUser().getUsername());
        model.addAttribute("Username",username);

        return "my_post";
    }
    @PostMapping("/post/{username}")
    public String AuthorsPost(@PathVariable("username") String username, Model model,
                              @RequestParam(required = true, defaultValue = "") String action,
                              @RequestParam(required = true, defaultValue = "") String id
    ){
        Long authorId = userService.findUserByUsername(username).getId();
        model.addAttribute("I",userService.getCurrentUser().getUsername());
        model.addAttribute("Username",username);
        boolean isDelete = false;
        if(action.equals("delete")){
            isDelete = true;
            messageSender.sendDeletePostRequest(Long.parseLong(id));

        }
        messageSender.sendGetAuthorPosts(authorId);
        List<Post> posts = postService.getAuthorPost(username);
        if(isDelete){
            posts = postService.isDeleted(authorId,Long.parseLong(id));
        }


        model.addAttribute("NewPost",posts);

        return "my_post";
    }

}