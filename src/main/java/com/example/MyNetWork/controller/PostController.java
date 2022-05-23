package com.example.MyNetWork.controller;

import com.example.MyNetWork.entity.PostsAndLike;
import com.example.MyNetWork.entity.User;
import com.example.MyNetWork.service.*;
import com.example.postapi.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Controller
public class PostController {
    @Autowired
    private KafkaMessageSender messageSender;
    @Autowired
    private UserService userService;
    @Autowired
    private GlobalService postService;
    @Autowired
    private ImageService imageService;



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
        var comment = postsAndLike.getComments();
        model.addAttribute("NewPost",posts);
        model.addAttribute("Like",like);
        model.addAttribute("Comment",comment);
        model.addAttribute("I",userService.getCurrentUser());



        return "news";
    }


    @PostMapping("/news")
    public String like( Model model,
                        @RequestParam(required = true, defaultValue = "") String action,
                        @RequestParam(required = true, defaultValue = "") String id,
                        @RequestParam(required = true, defaultValue = "") String text)
            throws ExecutionException, InterruptedException {
            addOrDelete(action,id,text,null);



        PostsAndLike  postsAndLike= postService.getNewsWithLike();
        var posts = postsAndLike.getPosts();
        var like = postsAndLike.getLikes();
        var comment = postsAndLike.getComments();
        model.addAttribute("NewPost",posts);
        model.addAttribute("Like",like);
        model.addAttribute("Comment",comment);
        model.addAttribute("I",userService.getCurrentUser());


        return "news";
    }
    public void addOrDelete(String action,String id, String text, Long authorId){
        Long userId = userService.getCurrentUser().getId();
        if(action.equals("addComment")){
            postService.saveComment(text,new BigInteger(id));
        }
        else if(action.equals("removeComment")){
            postService.deleteComment(new BigInteger(id));
        }
        else {
            BigInteger post_id = new BigInteger(id);
            postService.addOrRemoveLike(post_id,userId,action, authorId);
        }
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
        model.addAttribute("I",userService.getCurrentUser());
        model.addAttribute("Username",username);

        return "my_post";
    }




    @PostMapping("/post/{username}")
    public String AuthorsPost(@PathVariable("username") String username, Model model,
                              @RequestParam(required = true, defaultValue = "") String action,
                              @RequestParam(required = true, defaultValue = "") String id,
                              @RequestParam(required = true, defaultValue = "") String text

    ){
        model.addAttribute("I",userService.getCurrentUser());
        model.addAttribute("Username",username);
        Long authorId = userService.findUserByUsername(username).getId();
        addOrDelete(action,id,text,authorId);

        PostsAndLike  postsAndLike= postService.getMyPostWithLike(authorId);
        var posts = postsAndLike.getPosts();
        var like = postsAndLike.getLikes();
        var comment = postsAndLike.getComments();






        model.addAttribute("NewPost",posts);
        model.addAttribute("Like",like);
        model.addAttribute("Comment",comment);
        model.addAttribute("I",userService.getCurrentUser());

        return "my_post";
    }

}