
package com.example.postapi.repository;

import com.example.postapi.model.Post;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.scheduling.annotation.Async;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface PostRepo extends ReactiveCrudRepository<Post,BigInteger> {
    @Async
    public Flux<Post> findAllByAuthorId(Long id);




}