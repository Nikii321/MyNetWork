package com.example.MyNetWork.service;


import com.example.MyNetWork.entity.UsDetails;
import com.example.detailsapi.model.Details;

public interface DetailsService {
    public void addHashMap(Long id, Details details);
    public Details getDetails(Long id) throws InterruptedException;
    public void sendKafkaListId();
}