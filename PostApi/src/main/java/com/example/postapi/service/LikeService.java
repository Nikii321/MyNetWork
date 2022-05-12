package com.example.postapi.service;

import com.example.postapi.model.Like;
import com.example.postapi.model.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LikeService {
    public Flux<Long> findAllByPostId(Long postId);
    public Flux<Long> findAllByUserId(Long userId);
    public Mono<Like> saveLike(Long postId,Long userId);
    public Mono<Void> deleteLike(Long postId, Long userId);
    Mono<Like> findByPostIdAndIdUser(Long postId,Long userId);
}
