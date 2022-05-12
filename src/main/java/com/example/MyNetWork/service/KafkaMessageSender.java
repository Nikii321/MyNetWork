package com.example.MyNetWork.service;

import com.example.detailsapi.model.Details;
import com.example.postapi.model.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.util.Set;

import static com.example.MyNetWork.config.KafkaProducerConfig.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageSender implements MessageSender {
    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private final ObjectMapper objectMapper;

    @Override
    public void send(Post message) {
        log.info("send post for add message:{}", message);
        String messageAsString = message.PostToString();
        try {
            messageAsString = objectMapper.writeValueAsString(messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", message, ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_REQUESTS, messageAsString);
    }


    @Override
    public void send(Set<Long> SubscriberId, Long id) {
        log.info("send list<long> for news, message:{}", SubscriberId);
        String messageAsString = id+",";
        messageAsString += SubscriberId.toString().replace("[","").replace("]","").replace(" ","");
        try {
            messageAsString = objectMapper.writeValueAsString(messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", SubscriberId, ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_REQUESTS_NEWS, messageAsString);
    }
    @Override
    public void send(Details details) {
        log.info("send details message:{}", details);
        String messageAsString = details.UsDetailsToString();
        try {
            messageAsString = objectMapper.writeValueAsString(messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", details, ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_REQUESTS_DETAILS_ADD, messageAsString);
    }
    @Override
    public void send(Long id) {
        log.info("send details id message:{}", id);
        String messageAsString = String.valueOf(id);
        try {
            messageAsString = objectMapper.writeValueAsString(messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", id, ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_REQUESTS_DETAILS, messageAsString);
    }
    @Override
    public void sendDeletePostRequest(Long id) {
        log.info("send post id for delete:{} ", id);
        String messageAsString = String.valueOf(id);
        try {
            messageAsString = objectMapper.writeValueAsString(messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", id, ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_REQUESTS_POST_DELETE, messageAsString);
    }

    @Override
    public void sendGetAuthorPosts(Long id){
        String message = String.valueOf(id);
        log.info("send id for author post:{}", message);
        try {
            message = objectMapper.writeValueAsString(message);
        }
        catch (JsonProcessingException ex){
            log.error("can't serialize message:{}", message, ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_REQUESTS_GET_AUTHOR_POST, message);
    }
    public void  sendAddOrDeleteLike(Long postId, Long userId, String action){
        String message = userId+ " ; " + postId+" ; "+action;
        log.info("{} from {} to {}",action,userId, postId);
        try {
            message = objectMapper.writeValueAsString(message);
        }
        catch (JsonProcessingException ex){
            log.error("can't serialize message:{}", message, ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_GET_USER_LIKES_REQUESTS, message);
    }




}