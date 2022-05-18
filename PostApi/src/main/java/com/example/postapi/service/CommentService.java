package com.example.postapi.service;

import com.example.postapi.model.Comment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CommentService {
    public Flux<Comment> getComments(Long post);
    public Mono<Comment> saveComment(Long post_id, Long user_id, String text);
    public Mono<Void> deleteComment(Long post_id,Long user_id, String text);
    public Flux<Comment> findAllByManyPosts(List<Long> posts_id);
}
