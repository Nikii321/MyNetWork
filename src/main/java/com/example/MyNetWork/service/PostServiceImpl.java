package com.example.MyNetWork.service;

import com.example.MyNetWork.entity.PostsAndLike;
import com.example.MyNetWork.entity.User;
import com.example.postapi.model.Comment;
import com.example.postapi.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    UserService userService;
    @Autowired
    MessageSender kafkaMessageSender;
    @Autowired
    ImageService imageService;

    private final ConcurrentHashMap<Long, List<Post>> newsHashMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, List<Post>> authorsPostsHashMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, List<Long>> likeHashMap= new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, List<Comment>> commentHashMap= new ConcurrentHashMap<>();



    //<------------------------------------- Add in HashMap (Start) ------------------------------------->
    @Override
    public void addCommentHashMap( Long id, List<Comment> list) {

        commentHashMap.put(id,list);
    }
    @Override
    public void addListHashMap( Long id, List<Post> list) {

        newsHashMap.put(id,list);
    }
    @Override
    public void addUsersPostsHashMap( Long id, List<Post> list) {
        authorsPostsHashMap.put(id,list);
    }

    public void addLikePosts(Long id, List<Long> list){
        likeHashMap.put(id,list);
    }
    //<------------------------------------- Add in HashMap (Finish) ------------------------------------->



    @Override
    public void  sendNewPost(Post post){
        authorsPostsHashMap.put(post.getAuthorId(),null);
        kafkaMessageSender.send(post);
    }


    //<------------------------------------- Post and Like (Start) ------------------------------------->


    private CompletableFuture<List<Long>> getFutureLike(Long userId){
        return CompletableFuture.supplyAsync(()->this.getUsersLikes(userId));
    }
    public PostsAndLike getNewsWithLike(){
        Long id = userService.getCurrentUser().getId();
        CompletableFuture<List<Post>> completableFuturePost = CompletableFuture.supplyAsync(()-> this.getNews(id));
        PostsAndLike  postsAndLike = getPostAndLike(id,completableFuturePost);
        return postsAndLike;
    }

    public PostsAndLike getMyPostWithLike(Long id){
        Long user_id = userService.getCurrentUser().getId();
        CompletableFuture<List<Post>> completableFuturePost = CompletableFuture.supplyAsync(()-> this.getAuthorPost(id));

        return getPostAndLike(user_id,completableFuturePost);
    }
    private   PostsAndLike getPostAndLike(Long user_id,  CompletableFuture<List<Post>> completableFuturePost){
        CompletableFuture<List<Long>> completableFutureLike = getFutureLike(user_id);
        PostsAndLike postsAndLike = null;
        try {
            postsAndLike =   new PostsAndLike(completableFuturePost.get(),completableFutureLike.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return postsAndLike;
    }

    private List<Post> getNews(Long id)  {
        return getPosts(newsHashMap,id);
    }
    private List<Post> getPosts(ConcurrentHashMap<Long,List<Post>> hashMap,Long id){
        List<Post> posts = hashMap.get(id);
        var data = System.currentTimeMillis();
        while (posts == null){

            posts = hashMap.get(id);
            if(System.currentTimeMillis()-data>=1000){
                break;
            }
        }
        return posts;
    }
    private List<Long> getUsersLikes(Long id){

        List<Long> like = likeHashMap.get(id);
        var data = System.currentTimeMillis();

        while (like == null){
            like = likeHashMap.get(id);
            if(System.currentTimeMillis()-data>=1000){
                break;
            }
        }


        return like;
    }
    //<------------------------------------- Post and Like (Finished) ------------------------------------->


    //<------------------------------------- Add in Like (Start) ------------------------------------->


    public void addOrRemoveLike(Long postId,Long userId,String action,Long authorId){
        List<Post> posts;
        if(authorId==null) {
            posts = newsHashMap.get(userId);
        }
        else {
            posts = authorsPostsHashMap.get(userId);
        }

        List<Long> like = likeHashMap.get(userId);
        if(action.equals("add") && !likeHashMap.get(userId).contains(postId)){
           posts = addLike(posts,like,postId);
        }
        else if(action.equals("delete") && likeHashMap.get(userId).contains(postId)) {
            posts = deleteLike(posts,like,postId);
        }
        kafkaMessageSender.sendAddOrDeleteLike(postId, userId,action);
        addLikePosts(userId,like);
        if(authorId==null) {
            addListHashMap(userId, posts);
        }
        else {
            addUsersPostsHashMap(authorId,posts);
        }
    }


    private List<Post> addLike(List<Post> posts, List<Long> like, Long postId){
        posts = posts.parallelStream().peek(s->{
            if(Objects.equals(s.getId(), postId)){
                s.incrementPlus();
            }
        }).collect(Collectors.toList());
        like.add(postId);
        return posts;

    }
    private List<Post> deleteLike(List<Post> posts, List<Long> like, Long postId){
        posts = posts.parallelStream().peek(s->{
            if(Objects.equals(s.getId(), postId)){
                s.incrementMinus();
            }
        }).collect(Collectors.toList());
        like.remove(postId);
        return posts;
    }
    //<------------------------------------- Add in Like (Finish) ------------------------------------->

    private List<Post> getAuthorPost(Long id){

        return getPosts(authorsPostsHashMap,id);
    }

    public List<Post> delete(Long id, Long postId){
        kafkaMessageSender.sendDeletePostRequest(postId);
        List<Post> posts = authorsPostsHashMap.get(id);
        imageService.delete(posts, postId);

        authorsPostsHashMap.put(id, deletePostById(posts,postId));

        return authorsPostsHashMap.get(id);
    }
    private Post getPostById(List<Post> posts,Long postId){
        return  posts.parallelStream().filter(s->s.getId() == postId).findFirst().get();
    }
    private List<Post> deletePostById(List<Post> posts,Long postId){
        posts.remove(getPostById(posts,postId));
        return posts;
    }

    @Override
    public void sendKafkaListId() {
        User user = userService.getCurrentUser();
        Set<Long> idList = userService.getListIdUser(user);
        kafkaMessageSender.send(idList, user.getId());

    }

}