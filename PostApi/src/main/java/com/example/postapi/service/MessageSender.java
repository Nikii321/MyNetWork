package com.example.postapi.service;

import com.example.postapi.model.Post;

import java.util.List;

public interface MessageSender {
    public void send(Post post);
    public void send(List<Post> message,Long id);
}
