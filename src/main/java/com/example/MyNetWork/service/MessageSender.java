package com.example.MyNetWork.service;

import com.example.detailsapi.model.Details;
import com.example.postapi.model.Post;

import java.math.BigInteger;
import java.util.Set;

public interface MessageSender {
    public void send(Post message) ;
    public void send(Set<Long>  SubscriberId, Long id);
    public void send(Long id);
    public void send(Details details);
    public void  sendAddOrDeleteLike(BigInteger postId, Long userId, String action);
    public void sendDeletePostRequest(BigInteger id);
    public void sendGetAuthorPosts(Long id);
}