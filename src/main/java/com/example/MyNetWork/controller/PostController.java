package com.example.MyNetWork.controller;

import com.example.MyNetWork.entity.PostsAndLike;
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
import java.util.Objects;
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
        PostsAndLike  postsAndLike= postService.getNewsWithLike();
        var posts = postsAndLike.getPosts();
        var like = postsAndLike.getLikes();
        model.addAttribute("NewPost",posts);
        model.addAttribute("Like",like);



        return "news";
    }


    @PostMapping("/news")
    public String like( Model model,
                        @RequestParam(required = true, defaultValue = "") String action,
                        @RequestParam(required = true, defaultValue = "") String id)
            throws ExecutionException, InterruptedException {
        Long userId = userService.getCurrentUser().getId();
        Long post_id = Long.parseLong(id);

        postService.addOrRemoveLike(post_id,userId,action, null);
        PostsAndLike  postsAndLike= postService.getNewsWithLike();
        var posts = postsAndLike.getPosts();
        var like = postsAndLike.getLikes();


        model.addAttribute("NewPost",posts);
        model.addAttribute("Like",like);


        return "news";
    }


    @GetMapping("/post/{username}")
    public String AuthorsPost(@PathVariable("username") String username, Model model){
        Long id = userService.findUserByUsername(username).getId();
        messageSender.sendGetAuthorPosts(userService.findUserByUsername(username).getId());
        PostsAndLike  postsAndLike= postService.getMyPostWithLike(id);
        var posts = postsAndLike.getPosts();
        var like = postsAndLike.getLikes();
        model.addAttribute("NewPost",posts);
        model.addAttribute("Like",like);
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
        postService.sendKafkaListId();
        PostsAndLike  postsAndLike= postService.getMyPostWithLike(authorId);
        var posts = postsAndLike.getPosts();
        var like = postsAndLike.getLikes();
        model.addAttribute("I",userService.getCurrentUser().getUsername());
        model.addAttribute("Username",username);
        if(action.equals("remove")){
            postService.delete(authorId,Long.parseLong(id));

        }
        else{
            postService.addOrRemoveLike(Long.parseLong(id), userService.getCurrentUser().getId(),action,authorId);
        }
        messageSender.sendGetAuthorPosts(authorId);




        model.addAttribute("NewPost",posts);
        model.addAttribute("Like",like);
        System.out.println(like);

        return "my_post";
    }

}