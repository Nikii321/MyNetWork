package com.example.MyNetWork.service;

import com.example.postapi.model.Post;

import java.util.Set;

public interface MessageSender {
    public void send(Post message) ;
    public void send(Set<Long>  SubscriberId, Long id);
}
