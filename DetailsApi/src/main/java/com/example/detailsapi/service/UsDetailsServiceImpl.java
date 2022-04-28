package com.example.detailsapi.service;

import com.example.detailsapi.model.Details;
import com.example.detailsapi.repository.DetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UsDetailsServiceImpl implements UsDetailsService{
    @Autowired
    private DetailRepo usDetailRepo;

    public Mono<Details> findById(Long id){
        return usDetailRepo.findById(id);
    }
    public Mono<Details> save(Mono<Details> usDetailsMono){
        return usDetailsMono.flatMap(usDetailRepo::save);
    }
    public Mono<Void> delete(Long id){
        Mono<Void> mono =  usDetailRepo.findById(id).flatMap(this.usDetailRepo::delete);
        return mono;
    }
}
