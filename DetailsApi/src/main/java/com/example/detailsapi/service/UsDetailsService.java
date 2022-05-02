package com.example.detailsapi.service;

import com.example.detailsapi.model.Details;
import reactor.core.publisher.Mono;

public interface UsDetailsService {
    public Mono<Details> findById(Long id);
    public Mono<Void> delete(Long id);
    public Mono<Details> saveOrUpdate(Long id, Mono<Details> detailsMono);

}
