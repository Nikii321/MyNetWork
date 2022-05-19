package com.example.postapi.service;

import com.example.postapi.model.Comment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.List;

public interface CommentService {
    public Flux<Comment> getComments(BigInteger post);
    public Mono<Comment> saveComment(BigInteger post_id, Long user_id, String text);
    public Mono<Void> deleteComment(BigInteger post_id,Long user_id, String text);
    public Flux<Comment> findAllByManyPosts(List<BigInteger> posts_id);
}
