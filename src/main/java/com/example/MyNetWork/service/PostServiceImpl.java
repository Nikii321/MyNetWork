package com.example.MyNetWork.service;

import com.example.MyNetWork.entity.PostsAndLike;
import com.example.MyNetWork.entity.User;
import com.example.postapi.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
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

    private  HashMap<Long, List<Post>> listHashMap= new HashMap<>();
    private  HashMap<Long, List<Post>> usersPostsHashMap= new HashMap<>();
    private  HashMap<Long, List<Long>> likeHashMap= new HashMap<>();



    @Override
    public void addListHashMap( Long id, List<Post> list) {

        listHashMap.put(id,list);
    }
    @Override
    public void addUsersPostsHashMap( Long id, List<Post> list) {
        usersPostsHashMap.put(id,list);
    }
    @Override
    public void  sendNewPost(Post post){
        usersPostsHashMap.put(post.getAuthorId(),null);
        kafkaMessageSender.send(post);
    }

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
        return getPosts(listHashMap,id);
    }
    private List<Post> getPosts(HashMap<Long,List<Post>> hashMap,Long id){
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
    public void addOrRemoveLike(Long postId,Long userId,String action,Long authorId){
        List<Post> posts = listHashMap.get(userId);
        List<Long> like = likeHashMap.get(userId);
        if(action.equals("add") && !likeHashMap.get(userId).contains(postId)){
           addLike(posts,like,postId,authorId,userId);
        }
        else if(action.equals("delete") && likeHashMap.get(userId).contains(postId)) {
            deleteLike(posts,like,postId,authorId,userId);
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

    private void changeAddListHashMap(Long userId, List<Post> posts, Long authorId){
        if(authorId==null) {
            addListHashMap(userId, posts);
        }
        else {
            addUsersPostsHashMap(authorId,posts);
        }
    }
    private void addLike(List<Post> posts, List<Long> like, Long postId, Long authorId,Long userId){
        posts = posts.parallelStream().peek(s->{
            if(Objects.equals(s.getId(), postId)){
                s.incrementPlus();
            }
        }).collect(Collectors.toList());
        like.add(postId);
        changeAddListHashMap(userId, posts,authorId);

    }
    private void deleteLike(List<Post> posts, List<Long> like, Long postId, Long authorId,Long userId){
        posts = posts.parallelStream().peek(s->{
            if(Objects.equals(s.getId(), postId)){
                s.incrementMinus();
            }
        }).collect(Collectors.toList());
        like.remove(postId);
        changeAddListHashMap(userId, posts,authorId);
    }
    private List<Post> getAuthorPost(Long id){

        return getPosts(usersPostsHashMap,id);
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






    public List<Post> delete(Long id, Long postId){
        kafkaMessageSender.sendDeletePostRequest(postId);
        List<Post> posts = usersPostsHashMap.get(id);
        imageService.delete(posts, postId);
        usersPostsHashMap.put(id, posts);

        return usersPostsHashMap.get(id);
    }

    @Override
    public void sendKafkaListId() {
        User user = userService.getCurrentUser();
        Set<Long> idList = userService.getListIdUser(user);
        kafkaMessageSender.send(idList, user.getId());

    }

    public void addLikePosts(Long id, List<Long> list){
        likeHashMap.put(id,list);
    }






}