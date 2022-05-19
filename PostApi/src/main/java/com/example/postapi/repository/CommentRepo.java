package com.example.postapi.repository;

import com.example.postapi.model.Comment;
import com.example.postapi.model.Like;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.scheduling.annotation.Async;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.List;

public interface CommentRepo extends ReactiveCrudRepository<Comment,BigInteger> {
    @Async
    @Query("SELECT comment_id,post_id,user_id,text FROM comment_post INNER JOIN post ON comment_post.post_id = post.id where comment_post.post_id=:id;")
    Flux<Comment> findAllByPostId(BigInteger id);
    @Async
    @Query("INSERT INTO public.comment_post(post_id, user_id, comment_text) VALUES ( :post_id, :user_id, :text);")
    Mono<Comment> saveComment(BigInteger post_id,Long user_id, String text);
    @Async
    @Query("DELETE FROM public.comment_post WHERE post_id=:post_id and user_id = :user_id and text = :text; ")
    Mono<Void> deleteComment(BigInteger post_id,Long user_id, String text);

    @Async
    @Query("SELECT comment_id,post_id,user_id,text FROM comment_post INNER JOIN post ON comment_post.post_id = post.id where post_id in (:posts_id) LIMIT 10;")
    Flux<Comment> findAllByManyPosts(List<BigInteger> posts_id);
    @Async
    @Query("SELECT comment_id,post_id,user_id,text FROM comment_post INNER JOIN post ON comment_post.post_id = post.id where post_id in :posts_id LIMIT 10, OFFSET :start ;")
    Flux<Comment> findAllByManyPosts(List<BigInteger> posts_id, long start);


}

