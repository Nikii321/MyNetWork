package com.example.postapi.service;

import com.example.postapi.model.Model;
import com.example.postapi.model.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.postapi.config.KafkaProducerConfig.TOPIC_ADD_POST_RESPONSE;
import static com.example.postapi.config.KafkaProducerConfig.TOPIC_DELETE_POST_RESPONSE;
import static com.example.postapi.config.KafkaProducerConfig.TOPIC_UPDATE_POST_RESPONSE;



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
        log.info("send message:{}", message);
        String messageAsString = message.customToString();
        try {
            messageAsString = objectMapper.writeValueAsString(messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", message, ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_ADD_POST_RESPONSE, messageAsString);
    }
    @Override
    public void send(List<? extends Model> message, Long id, String TOPIC_SOMETHING_RESPONSE) {
        log.info("send message:{}", message);
        String messageAsString =id+" ; ";
        for(int i=0;i<message.size();i++){

            messageAsString += message.get(i).customToString();
            if(i!= message.size()-1){
                messageAsString+=" ; ";
            }
        }

        try {
            messageAsString = objectMapper.writeValueAsString(messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", message, ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_SOMETHING_RESPONSE, messageAsString);
    }

    public void sendForLike(List<Long> message, Long id, String TOPIC_SOMETHING_RESPONSE) {
        log.info("send message:{}", message);
        String messageAsString =id+" ; ";
        for(int i=0;i<message.size();i++){

            messageAsString += message.get(i);
            if(i!= message.size()-1){
                messageAsString+=" ; ";
            }
        }

        try {
            messageAsString = objectMapper.writeValueAsString(messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", message, ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_SOMETHING_RESPONSE, messageAsString);
    }
    @Override
    public void send(Void unused) {
        log.info("post is deleted");
        var messageAsString="post is deleted";
        try {
            messageAsString = objectMapper.writeValueAsString(messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", "post is deleted", ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_DELETE_POST_RESPONSE, messageAsString);
    }
    @Override
    public void sendUpdate(Post post) {
        log.info("send message:{}", post);
        var messageAsString = post.customToString();
        try {
            messageAsString = objectMapper.writeValueAsString(messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", post, ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_UPDATE_POST_RESPONSE, messageAsString);
    }





}