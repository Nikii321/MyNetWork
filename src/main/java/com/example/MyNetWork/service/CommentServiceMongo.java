package com.example.MyNetWork.service;

import com.example.postapi.model.Comment;
import com.example.postapi.model.Post;

import java.math.BigInteger;
import java.util.List;

public interface CommentServiceMongo {
    public void  saveComment(List<Comment> listComment);
    public List<Comment> findFirstsComment(List<Post> posts);
    public List<Comment> findPostId(Post post);
    public void saveComment(Comment comment);

    public void deleteComment(BigInteger id);
}
