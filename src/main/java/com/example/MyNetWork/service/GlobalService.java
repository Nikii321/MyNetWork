package com.example.MyNetWork.service;

import com.example.MyNetWork.entity.PostsAndLike;
import com.example.postapi.model.Comment;
import com.example.postapi.model.Post;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface GlobalService {
    public void sendKafkaListId();
    public void sendNewPost(Post post);

    public void savePost( Long id, List<Post> list);
    public void  saveNews(Long id,List<Post> post);
    public void saveLike(Long id, List<BigInteger> list);
    public void saveComments(  List<Comment> list);

    public PostsAndLike getMyPostWithLike(Long id);
    public PostsAndLike getNewsWithLike();


    public List<Post> delete(Long id, BigInteger postId);
    public void addOrRemoveLike(BigInteger postId,Long userId,String action,Long authorId);
}