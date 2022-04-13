package com.example.MyNetWork.service;

import com.example.MyNetWork.entity.UsDetails;
import org.springframework.stereotype.Service;

public interface DetailsService {
    public void save(UsDetails usDetails);
    public UsDetails findById(Long id);
}