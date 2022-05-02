package com.example.MyNetWork.service;

import com.example.MyNetWork.entity.User;
import com.example.detailsapi.model.Details;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class DetailsServiceImpl implements DetailsService{

    @Autowired
    UserService userService;

    @Autowired
    MessageSender kafkaMessageSender;

    private  HashMap<Long, Details> detailsHashMap= new HashMap<>();

    public HashMap<Long, Details> getDetailsHashMap() {
        return detailsHashMap;
    }

    public void addHashMap( Long id, Details details) {

        detailsHashMap.put(id,details);
    }

    public Details getDetails(Long id) throws InterruptedException {

        Details details = detailsHashMap.get(id);
        Details newDetails = null;

        int i=0;
        var data =  System.currentTimeMillis();
        while (newDetails == null || newDetails.equals(details) ) {

            newDetails = detailsHashMap.get(id);
            if(System.currentTimeMillis()-data>=5000){
                return details;
            }
        }
        return newDetails;
    }

    public void sendKafkaListId() {
        User user = userService.getCurrentUser();
        kafkaMessageSender.send(user.getId());

    }
}