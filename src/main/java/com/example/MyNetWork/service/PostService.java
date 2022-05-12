package com.example.MyNetWork.service;

import com.example.MyNetWork.entity.PostsAndLike;
import com.example.postapi.model.Post;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PostService {
    public void sendKafkaListId();
    public void addListHashMap( Long id, List<Post> list);
    public void addUsersPostsHashMap( Long id, List<Post> list);
    public void  sendNewPost(Post post);
    public void addLikePosts(Long id, List<Long> list);
    public List<Post> delete(Long id, Long postId);
    public List<Long> getLike(Long id);
    public PostsAndLike getNewsWithLike();
    public void addOrRemoveLike(Long postId,Long userId,String action,Long authorId);
    public PostsAndLike getMyPostWithLike(Long id);
    public List<Post> getListMyPost(Long id);
    public List<Post> getListPost(Long id);
    public List<Long> getListLike();
}