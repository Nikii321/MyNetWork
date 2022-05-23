package com.example.MyNetWork.service;

import com.example.detailsapi.model.Details;
import com.example.postapi.model.Comment;
import com.example.postapi.model.Model;
import com.example.postapi.model.Post;

import java.math.BigInteger;
import java.util.Set;

public interface MessageSender {
    public void send(Model message, String REQUEST);
    public void send(Set<Long>  SubscriberId, Long id);
    public void send(Long id);
    public void send(Details details);
    public void  sendAddOrDeleteLike(BigInteger postId, Long userId, String action);
    public void sendDeletePostRequest(BigInteger id);
    public void sendGetAuthorPosts(Long id);
    public void sendComment(Comment comment,String SOME_REQUEST);
}