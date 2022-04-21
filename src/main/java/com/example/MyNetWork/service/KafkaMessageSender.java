package com.example.MyNetWork.service;

import com.example.postapi.model.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.example.MyNetWork.config.KafkaProducerConfig.TOPIC_RATE_REQUESTS;
import static com.example.MyNetWork.config.KafkaProducerConfig.TOPIC_RATE_REQUESTS_NEWS;

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
        kafkaTemplate.send(TOPIC_RATE_REQUESTS, messageAsString);
    }

    @Override
    public void send(Set<Long> SubscriberId, Long id) {
        log.info("send message:{}", SubscriberId);
        String messageAsString = id+",";
        messageAsString += SubscriberId.toString().replace("[","").replace("]","").replace(" ","");
        try {
            messageAsString = objectMapper.writeValueAsString(messageAsString);
        } catch (JsonProcessingException ex) {
            log.error("can't serialize message:{}", SubscriberId, ex);
            throw new RuntimeException();
        }
        kafkaTemplate.send(TOPIC_RATE_REQUESTS_NEWS, messageAsString);
    }




}
