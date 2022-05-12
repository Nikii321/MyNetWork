package com.example.MyNetWork.service;

import com.example.MyNetWork.entity.PostsAndLike;
import com.example.MyNetWork.entity.User;
import com.example.postapi.model.Like;
import com.example.postapi.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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

    public List<Post> getListMyPost(Long id){
        return usersPostsHashMap.get(id);
    }
    public List<Post> getListPost(Long id){
        return listHashMap.get(id);
    }
    public List<Long> getListLike(){
        return likeHashMap.get(userService.getCurrentUser().getId());
    }


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

    public PostsAndLike getNewsWithLike(){
        Long id = userService.getCurrentUser().getId();
        CompletableFuture<List<Post>> completableFuturePost = CompletableFuture.supplyAsync(()-> this.getNews(id));
        CompletableFuture<List<Long>> completableFutureLike = CompletableFuture.supplyAsync(()-> this.getUsersLikes(id));

        PostsAndLike postsAndLike = null;
        try {

            postsAndLike =   new PostsAndLike(completableFuturePost.get(),completableFutureLike.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return postsAndLike;
    }
    public PostsAndLike getMyPostWithLike(Long id){
        Long user_id = userService.getCurrentUser().getId();
        CompletableFuture<List<Post>> completableFuturePost = CompletableFuture.supplyAsync(()-> this.getAuthorPost(id));
        CompletableFuture<List<Long>> completableFutureLike = CompletableFuture.supplyAsync(()-> this.getUsersLikes(user_id));

        PostsAndLike postsAndLike = null;
        try {

            postsAndLike =   new PostsAndLike(completableFuturePost.get(),completableFutureLike.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return postsAndLike;
    }

    private List<Post> getNews(Long id)  {
        List<Post> posts = listHashMap.get(id);
        var data = System.currentTimeMillis();
        while (posts == null){

            posts = listHashMap.get(id);
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
            posts = posts.parallelStream().peek(s->{
                if(Objects.equals(s.getId(), postId)){
                    s.incrementPlus();
                }
            }).collect(Collectors.toList());
            like.add(postId);
        }
        else if(action.equals("delete") && likeHashMap.get(userId).contains(postId)) {
            posts = posts.parallelStream().peek(s->{
                if(Objects.equals(s.getId(), postId)){
                    s.incrementMinus();
                }
            }).collect(Collectors.toList());
            like.remove(postId);
        }
        kafkaMessageSender.sendAddOrDeleteLike(postId, userId,action);
        addLikePosts(userId,like);
        System.out.println("\n\n\n\n\n");
        posts.stream().forEach(System.out::println);
        System.out.println("\n\n\n\n\n");
        if(authorId==null) {
            addListHashMap(userId, posts);
        }
        else {
            addUsersPostsHashMap(authorId,posts);
        }


    }
    private List<Post> getAuthorPost(Long id){
        List<Post> newPosts = usersPostsHashMap.get(id);

        var data = System.currentTimeMillis();
        while (newPosts == null){

            newPosts = usersPostsHashMap.get(id);
            if(System.currentTimeMillis()-data>=1000){
                break;
            }
        }
        return newPosts;
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





    private List<Long> getPostsId(List<Post> posts){
        return posts.stream().mapToLong(Post::getId).boxed().collect(Collectors.toList());
    }
    public List<Post> delete(Long id, Long postId){
        kafkaMessageSender.sendDeletePostRequest(postId);
        List<Post> posts = usersPostsHashMap.get(id);
        imageService.delete(posts.parallelStream().
                filter(s->s.getId()==(postId)).
                collect(Collectors.toList()).get(0).getPath());
        posts.remove(postId);
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
    public List<Long> getLike(Long id){
        return likeHashMap.get(id);
    }




}