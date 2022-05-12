package com.example.postapi.repository;

import com.example.postapi.model.Like;
import com.example.postapi.model.Post;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.scheduling.annotation.Async;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LikeRepo extends ReactiveCrudRepository<Like,Long> {

    @Async
    @Query("SELECT * FROM public.like where post_id = :postId and  user_id = :userId;")
    Mono<Like> findByPostIdAndIdUser(Long postId,Long userId);
    @Async
    @Query("SELECT user_id FROM public.like where post_id = :postId;")
    Flux<Long> findAllByPostId(Long postId);
    @Async
    @Query("SELECT post_id FROM public.like where user_id = :userId;")
    Flux<Long> findAllByIdUser(Long userId);
    @Async
    @Query("SELECT * FROM public.like;")
    Flux<Like> findAll();
    @Async
    @Query("INSERT INTO public.like(id,user_id, post_id) VALUES (:id, :userId, :postId);")
    Mono<Like> save(Long id,Long postId,Long userId);
    @Async
    @Query("DELETE FROM public.like WHERE post_id = :postId and user_id = :userId;")
    Mono<Void> deleteByPostIdAndAndIdUser(Long postId,Long userId);
    @Async
    @Query("SELECT max(id) FROM public.like")
    Long getMaxId();


}
