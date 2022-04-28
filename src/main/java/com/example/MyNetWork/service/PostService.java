package com.example.MyNetWork.service;

import com.example.postapi.model.Post;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PostService {
    public void sendKafkaListId();
    public List<Post> getNews() throws ExecutionException, InterruptedException;
    public void addListHashMap( Long id, List<Post> list);
}
