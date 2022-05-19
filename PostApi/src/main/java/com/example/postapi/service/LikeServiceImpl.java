package com.example.postapi.service;

import com.example.postapi.model.Like;
import com.example.postapi.model.Post;
import com.example.postapi.repository.LikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Service
public class LikeServiceImpl implements LikeService{
    @Autowired
    private LikeRepo likeRepo;



    public Mono<Like> findByPostIdAndIdUser(BigInteger postId, Long userId){
        return likeRepo.findByPostIdAndIdUser(postId,userId);
    }

    public Mono<Like> saveLike(BigInteger postId, Long userId){
        return likeRepo.save(postId,userId);
    }
    public Mono<Void> deleteLike(BigInteger postId, Long userId){
        return likeRepo.deleteByPostIdAndAndIdUser(postId,userId);
    }
    public Flux<Long> findAllByPostId(BigInteger postId){
        return likeRepo.findAllByPostId(postId);
    }
    public Flux<Long> findAllByUserId(Long userId){
        return likeRepo.findAllByIdUser(userId);
    }

}
