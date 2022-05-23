package com.example.MyNetWork.service;

import com.example.postapi.model.Post;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PostServiceMongo {
    public void savePosts(Long id, List<Post> postList);
    public void saveNews(Long id,List<Post> postList);
    public CompletableFuture<List<Post>> getNewsMongo(Long userId);
    public CompletableFuture<List<Post>> getMyPostMongo(Long id);
    public void saveOnePost(Long id);
}
