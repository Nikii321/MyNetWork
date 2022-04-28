package com.example.detailsapi.repository;

import com.example.detailsapi.model.Details;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

public interface DetailRepo extends ReactiveCrudRepository<Details,Long> {
    public Mono<Details> findById(Long id);



}
