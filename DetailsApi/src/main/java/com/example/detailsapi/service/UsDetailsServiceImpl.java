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
    private Mono<Details> save(Mono<Details> usDetailsMono) {
        return usDetailsMono.flatMap(usDetailRepo::save);
    }
    public Mono<Details> update(Long id, final Mono<Details> detailsMono){
        return this.findById(id)
                .flatMap(p -> detailsMono.map(u -> {
                    p.setId(u.getId());
                    p.setSurnameUser(u.getSurnameUser());
                    p.setNameUser(u.getNameUser());
                    p.setUniversity(u.getUniversity());
                    p.setWork(u.getWork());
                    p.setPhone(u.getPhone());
                    p.setCity(u.getCity());
                    p.setBirthday(u.getBirthday());
                    p.setPathImage(u.getPathImage());
                    return p;
                }))
                .flatMap(p -> usDetailRepo.save(p));
    }
    public Mono<Details> saveOrUpdate(Long id, Mono<Details> detailsMono){
        return this.findById(id).flatMap(p->detailsMono.flatMap(s->{
            return this.update(s.getId(),Mono.just(s));
        })).switchIfEmpty(this.save(detailsMono));
    }


    public Mono<Void> delete(Long id){
        Mono<Void> mono =  usDetailRepo.findById(id).flatMap(this.usDetailRepo::delete);
        return mono;
    }
}
