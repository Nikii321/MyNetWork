package com.example.postapi.service;

import com.example.postapi.model.Post;

import java.util.List;

public interface MessageSender {
    public void send(Post post);
    public void send(List<Post> message,Long id, String TOPIC_SOMETHING_RESPONSE);
    public void send(Void unused);
    public void sendUpdate(Post post);
    public void sendForLike(List<Long> message, Long id, String TOPIC_SOMETHING_RESPONSE);
}