package com.example.detailsapi.repository;

import com.example.detailsapi.model.Details;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import org.springframework.scheduling.annotation.Async;
import reactor.core.publisher.Mono;

public interface DetailRepo extends ReactiveCrudRepository<Details,Long> {


    @Async
    public Mono<Details> findById(Long id);


}
