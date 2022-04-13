package com.example.MyNetWork.service;

import com.example.MyNetWork.Repository.DetailsRepo;
import com.example.MyNetWork.entity.UsDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetailsServiceImpl implements DetailsService{
    @Autowired
    private DetailsRepo detailsRepo;
    public UsDetails findById(Long id){
        return  detailsRepo.findById(id).orElse(null);
    }
    public void save(UsDetails usDetails){
        detailsRepo.save(usDetails);
    }
}