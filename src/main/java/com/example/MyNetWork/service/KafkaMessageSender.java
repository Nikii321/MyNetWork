package com.example.MyNetWork.service;

import com.example.postapi.model.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
        kafkaTemplate.send("RATE_REQUESTS", messageAsString);
    }

}
