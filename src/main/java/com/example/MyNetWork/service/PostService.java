package com.example.MyNetWork.service;

import com.example.postapi.model.Post;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PostService {
    public void sendKafkaListId();
    public List<Post> getNews() throws ExecutionException, InterruptedException;
    public void addListHashMap( Long id, List<Post> list);
    public List<Post> getAuthorPost(String username);
    public void addUsersPostsHashMap( Long id, List<Post> list);
    public List<Post> isDeleted(Long id, Long PostId);
    public void  sendNewPost(Post post);
}
