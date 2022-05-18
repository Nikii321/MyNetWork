package com.example.MyNetWork.service;


import com.example.MyNetWork.entity.User;
import com.example.detailsapi.model.Details;

public interface DetailsService {
    public void addHashMap(Long id, Details details);
    public Details getDetails(Long id);
    public void sendKafkaListId();
    public void changeDetails(Details details, User user);
}