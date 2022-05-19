package com.example.postapi.service;

import com.example.postapi.model.Comment;
import com.example.postapi.model.Post;
import com.example.postapi.repository.CommentRepo;
import com.example.postapi.repository.PostRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Comparator;

@Service
@Slf4j
public class PostServiceImpl implements PostService{
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private CommentRepo commentRepo;

    public Flux<Post> showAll(){
        log.info("Start show");
        return postRepo.findAll();
    }
    public Mono<Post> incrementPlus(BigInteger postId){
        return postRepo.findById(postId).map(s->{
            s.incrementPlus();
            return s;
        }).flatMap(postRepo::save);

    }
    public Mono<Post> incrementMinus(BigInteger postId){
        return postRepo.findById(postId).map(s->{
            s.incrementMinus();
            return s;
        }).flatMap(postRepo::save);

    }
    public Mono<Post> showByID(BigInteger id){


        return postRepo.findById(id);
    }
    public Flux<Post> showByAuthorId(Long id){


        return postRepo.findAllByAuthorId(id);
    }
    public Mono<Post> save(Mono<Post> newPost){

        return newPost.flatMap(s ->{
            s.setDate(Instant.ofEpochMilli(System.currentTimeMillis()));
            return postRepo.save(s);
        });

    }
    public Mono<Post> update(Mono<Post> newPost,BigInteger id){

        delete(id);

        return newPost.flatMap(s ->{
            s.setDate(Instant.ofEpochMilli(System.currentTimeMillis()));
            return postRepo.save(s);
        });

    }
    public Mono<Void> delete(BigInteger id){
        Mono<Void> mono =  postRepo.findById(id).flatMap(this.postRepo::delete);
        return mono;
    }
    public Flux<Post> showUserNews(Flux<Long> authorId){

        return authorId.flatMap(this::showByAuthorId).sort(Comparator.comparing(Post::getDate).reversed());
    }

    public Flux<Post> getPopularPosts(Long id){
        return postRepo.findAll().filter(s->s.getAuthorId()!=id).sort(Comparator.comparing(Post::getCountLike).reversed()).take(10);
    }


}