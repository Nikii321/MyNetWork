package com.example.postapi.service;

import com.example.postapi.model.Comment;
import com.example.postapi.repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepo commentRepo;
    @Override
    public Flux<Comment> getComments(Long post){
        return commentRepo.findAllByPostId(post);
    }
    @Override
    public Mono<Comment> saveComment(Long post_id,Long user_id, String text){
        return  commentRepo.saveComment(post_id,user_id,text);
    }
    @Override
    public Mono<Void> deleteComment(Long post_id,Long user_id, String text){
        return commentRepo.deleteComment(post_id,user_id,text);
    }
    @Override
    public Flux<Comment> findAllByManyPosts(List<Long> posts_id){
        return commentRepo.findAllByManyPosts(posts_id);
    }
}
