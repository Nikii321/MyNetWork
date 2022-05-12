package com.example.postapi.service;

import com.example.postapi.model.Like;
import com.example.postapi.model.Post;
import com.example.postapi.repository.LikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class LikeServiceImpl implements LikeService{
    @Autowired
    private LikeRepo likeRepo;

    private static Long id =10L;


    public Mono<Like> findByPostIdAndIdUser(Long postId, Long userId){
        return likeRepo.findByPostIdAndIdUser(postId,userId);
    }

    public Mono<Like> saveLike(Long postId,Long userId){
        return likeRepo.save(id++,postId,userId);
    }
    public Mono<Void> deleteLike(Long postId, Long userId){
        return likeRepo.deleteByPostIdAndAndIdUser(postId,userId);
    }
    public Flux<Long> findAllByPostId(Long postId){
        return likeRepo.findAllByPostId(postId);
    }
    public Flux<Long> findAllByUserId(Long userId){
        return likeRepo.findAllByIdUser(userId);
    }

}
