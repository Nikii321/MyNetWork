package com.example.MyNetWork.config;

import com.example.MyNetWork.service.DetailsService;
import com.example.MyNetWork.service.PostService;
import com.example.MyNetWork.service.UserService;
import com.example.detailsapi.model.Details;
import com.example.postapi.model.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableKafka
public class KafkaConsumerConfig {
    public static final String TOPIC_RATE_REQUESTS = "ADD_POST_RESPONSE";
    public static final String TOPIC_RATE_RESPONSE_NEWS = "NEWS_SHOW_RESPONSE";
    public static final String TOPIC_RATE_REQUESTS_DETAILS_ADD = "ADD_DETAILS_RESPONSE";
    public static final String TOPIC_RATE_REQUESTS_DETAILS = "DETAILS_SHOW_RESPONSE";

    public static final String GROUP_ID_POST = "postApi";
    public static final String GROUP_ID_DETAILS = "userDetailsApi";

    private final ObjectMapper objectMapper;



    @Autowired
    PostService postService;
    @Autowired
    DetailsService detailsService;

    @Bean
    public NewTopic topic3 () {
        return new NewTopic("baeldung", 1, (short) 1);
    }

    @KafkaListener(groupId = GROUP_ID_POST, topics = TOPIC_RATE_REQUESTS)
    public void  rateRequestListen(String msgAsString) {
        Post message = new Post();
        try {
            message.takeData(objectMapper.readValue(msgAsString,String.class));

        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }
    }
    @KafkaListener(groupId = GROUP_ID_POST, topics = TOPIC_RATE_RESPONSE_NEWS)
    public void showNewsResponse(String msgAsString) {
        String[] data;
        try {
            data = objectMapper.readValue(msgAsString,String.class).split(" ; ");

        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }
        List<Post> list = new ArrayList<>();
        Long id =0L;
        for (String tmp :data){
            if(!tmp.contains("text")){
                id = Long.parseLong(tmp);
                continue;
            }
            Post post = new Post();
            post.takeData(tmp);
            list.add(post);
        }
        postService.addListHashMap(id,list);

    }
    @KafkaListener(groupId = GROUP_ID_DETAILS, topics = TOPIC_RATE_REQUESTS_DETAILS_ADD)
    public void  addDetailsResponse(String msgAsString) {
        Details message = new Details();
        try {
            message.takeData(objectMapper.readValue(msgAsString,String.class));

        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }
        System.out.println(message);
        detailsService.addHashMap(message.getId(),message);

    }
    @KafkaListener(groupId = GROUP_ID_DETAILS, topics = TOPIC_RATE_REQUESTS_DETAILS)
    public void showDetailsResponse(String msgAsString) {
        Details usDetails= new Details();
        try {
            usDetails.takeData(objectMapper.readValue(msgAsString,String.class));

        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }

    }

}
