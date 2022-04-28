package com.example.postapi.service;

import com.example.postapi.model.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.postapi.config.KafkaProducerConfig.TOPIC_RATE_RESPONSE;
import static com.example.postapi.config.KafkaProducerConfig.TOPIC_RATE_RESPONSE_NEWS;

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
        String messageAsString = message.PostToString();
        try {
            messageAsString = objectMapper.writeValueAsString(messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", message, ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_RATE_RESPONSE, messageAsString);
    }
    @Override
    public void send(List<Post> message, Long id) {
        log.info("send message:{}", message);
        String messageAsString =id+" ; ";
        for(int i=0;i<message.size();i++){

            messageAsString += message.get(i).PostToString();
            if(i!= message.size()-1){
                messageAsString+=" ; ";
            }
        }
        System.out.println(messageAsString);
        try {
            messageAsString = objectMapper.writeValueAsString(messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", message, ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_RATE_RESPONSE_NEWS, messageAsString);
    }
}
