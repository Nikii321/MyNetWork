package com.example.postapi.service;

import com.example.postapi.model.Like;
import com.example.postapi.model.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface LikeService {
    public Flux<Long> findAllByPostId(BigInteger postId);
    public Flux<Long> findAllByUserId(Long userId);
    public Mono<Like> saveLike(BigInteger postId,Long userId);
    public Mono<Void> deleteLike(BigInteger postId, Long userId);
    Mono<Like> findByPostIdAndIdUser(BigInteger postId,Long userId);
}
