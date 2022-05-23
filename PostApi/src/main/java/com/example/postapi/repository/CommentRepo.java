package com.example.postapi.repository;

import com.example.postapi.model.Comment;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.scheduling.annotation.Async;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.List;

public interface CommentRepo extends ReactiveCrudRepository<Comment,BigInteger> {
    @Async
    @Query("SELECT id,post_id,user_id,text FROM public..comment_post INNER JOIN public.post ON public.comment_post.post_id = public.post.id where public.comment_post.post_id=:id;")
    Flux<Comment> findAllByPostId(BigInteger id);
    @Async
    @Query("INSERT INTO public.comment_post(post_id, user_id, comment_text,comment_author) VALUES ( :post_id, :user_id, :text, :comment_author);")
    Mono<Comment> saveComment(BigInteger post_id,Long user_id, String text, String comment_author);
    @Async
    @Query("DELETE FROM public.comment_post WHERE post_id=:post_id and user_id = :user_id and comment_text = :text; ")
    Mono<Void> deleteComment(BigInteger post_id,Long user_id, String text);

    @Async
    @Query("SELECT  comment_id,post_id,user_id,comment_text,comment_author FROM public.post INNER JOIN public.comment_post ON public.comment_post.post_id = public.post.id where post_id in (:posts_id) LIMIT 10;")
    Flux<Comment> findAllByManyPosts(List<BigInteger> posts_id);
    @Async
    @Query("SELECT comment_id,post_id,user_id,comment_text FROM public.post INNER JOIN public.comment_post ON comment_post.post_id = post.id where post_id in (:posts_id) LIMIT 10, OFFSET :start ;")
    Flux<Comment> findAllByManyPosts(List<BigInteger> posts_id, long start);
    @Query("SELECT * FROM public.comment_post ")
    Flux<Comment> findAll();


}

