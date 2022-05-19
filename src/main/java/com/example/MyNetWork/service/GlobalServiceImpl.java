package com.example.MyNetWork.service;

import com.example.MyNetWork.entity.PostsAndLike;
import com.example.MyNetWork.entity.User;
import com.example.postapi.model.Comment;
import com.example.postapi.model.Post;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

@Service
public class GlobalServiceImpl implements GlobalService {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageSender kafkaMessageSender;
    @Autowired
    private ImageService imageService;
    @Autowired
    private PostServiceMongo postServiceMongo;
    @Autowired
    private LikeServiceMongo likeServiceMongo;
    @Autowired
    private CommentServiceMongo commentServiceMongo;
    //<------------------------------------------------KAFKA------------------------------------------------>
    @Override
    public void sendKafkaListId() {
        User user = userService.getCurrentUser();
        Set<Long> idList = userService.getListIdUser(user);
        kafkaMessageSender.send(idList, user.getId());
    }
    public void sendNewPost(Post post){
        kafkaMessageSender.send(post);
    }
    //<------------------------------------------------KAFKA------------------------------------------------>



    //<------------------------------------------------SAVE------------------------------------------------->
    public void savePost( Long id, List<Post> list){
        postServiceMongo.savePost(id,list);
    }
    public void  saveNews(Long id,List<Post> post){
        postServiceMongo.saveNews(id,post);
    }
    public void saveLike(Long id, List<BigInteger> list){
        likeServiceMongo.saveLike(id, list);
    }
    public void saveComments(  List<Comment> list){
        commentServiceMongo.saveComment(list);
    }
    //<------------------------------------------------SAVE------------------------------------------------->



    //<------------------------------------------------GET POST AND LIKE------------------------------------------------->
    @SneakyThrows
    public PostsAndLike getNewsWithLike(){
        Long id = userService.getCurrentUser().getId();
        return new PostsAndLike(postServiceMongo.getNewsMongo(id).get(),likeServiceMongo.getUserLike().get());
    }
    @SneakyThrows
    public PostsAndLike getMyPostWithLike(Long id){
        return new PostsAndLike(postServiceMongo.getMyPostMongo(id).get(),likeServiceMongo.getUserLike().get());
    }
    //<------------------------------------------------GET POST AND LIKE------------------------------------------------->


    @SneakyThrows
    public List<Post> delete(Long id, BigInteger postId){
        kafkaMessageSender.sendDeletePostRequest(postId);
        List<Post> posts = postServiceMongo.getMyPostMongo(id).get();
        imageService.delete(posts, postId);
        var newPost =  deletePostById(posts,postId);
        postServiceMongo.savePost(id, newPost);
        return newPost;
    }
    private Post getPostById(List<Post> posts,BigInteger postId){
        return  posts.parallelStream().filter(s->s.getId().equals(postId)).findFirst().get();
    }
    private List<Post> deletePostById(List<Post> posts,BigInteger postId){
        posts.remove(getPostById(posts,postId));
        return posts;
    }

    public void addOrRemoveLike(BigInteger postId,Long userId,String action,Long authorId){
        likeServiceMongo.addOrRemoveLike(postId,userId,action,authorId);
    }



}