package com.example.postapi.repository;

import com.example.postapi.model.Post;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostRepo extends ReactiveCrudRepository<Post,Long> {
    public Flux<Post> findAllByAuthorId(Long id);
    public Mono<Post> findById(Long id);


}
