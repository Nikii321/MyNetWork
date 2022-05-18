package com.example.postapi.service;


import com.example.postapi.model.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostService {
    public Flux<Post> showAll();
    public Mono<Post> showByID(Long id);
    public Flux<Post> showByAuthorId(Long id);
    public Mono<Post> save(Mono<Post> post);
    public Mono<Post> update(Mono<Post> newPost,Long id);
    public Mono<Void> delete(Long id);
    public Flux<Post> showUserNews(Flux<Long> authorId);
    public Mono<Post> incrementPlus(Long postId);
    public Mono<Post> incrementMinus(Long postId);
    public Flux<Post> getPopularPosts(Long id);

}