package com.example.postapi.service;

import com.example.postapi.model.Post;
import com.example.postapi.repository.PostRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@Slf4j
public class PostServiceImpl implements PostService{
    @Autowired
    private PostRepo postRepo;

    public Flux<Post> showAll(){
        log.info("Start show");
        return postRepo.findAll();
    }
    public Mono<Post> showByID(Long id){


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
    public Mono<Post> update(Mono<Post> newPost,Long id){

        delete(id);

        return newPost.flatMap(s ->{
            s.setDate(Instant.ofEpochMilli(System.currentTimeMillis()));
            return postRepo.save(s);
        });

    }
    public Mono<Void> delete(Long id){
        Mono<Void> mono =  postRepo.findById(id).flatMap(this.postRepo::delete);
        return mono;
    }
    public Flux<Post> showUserNews(Flux<Long> authorId){

        return authorId.flatMap(this::showByAuthorId);
    }


}
