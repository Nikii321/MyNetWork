package com.example.MyNetWork.service;

import com.example.postapi.model.Comment;

import java.util.List;

public interface CommentServiceMongo {
    public void  saveComment(List<Comment> listComment);
}
