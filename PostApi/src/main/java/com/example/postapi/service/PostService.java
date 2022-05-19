package com.example.postapi.service;


import com.example.postapi.model.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface PostService {
    public Flux<Post> showAll();
    public Mono<Post> showByID(BigInteger id);
    public Flux<Post> showByAuthorId(Long id);
    public Mono<Post> save(Mono<Post> post);
    public Mono<Post> update(Mono<Post> newPost,BigInteger id);
    public Mono<Void> delete(BigInteger id);
    public Flux<Post> showUserNews(Flux<Long> authorId);
    public Mono<Post> incrementPlus(BigInteger postId);
    public Mono<Post> incrementMinus(BigInteger postId);
    public Flux<Post> getPopularPosts(Long id);

}