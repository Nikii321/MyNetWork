package com.example.MyNetWork.service;

import com.example.detailsapi.model.Details;
import com.example.postapi.model.Post;

import java.util.Set;

public interface MessageSender {
    public void send(Post message) ;
    public void send(Set<Long>  SubscriberId, Long id);
    public void send(Long id);
    public void send(Details details);
    public void sendUpdatePostRequest(Post message);
    public void sendDeletePostRequest(Long id);
    public void sendGetAuthorPosts(Long id);
}
