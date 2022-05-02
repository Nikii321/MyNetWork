package com.example.detailsapi.config;


import com.example.detailsapi.model.Details;
import com.example.detailsapi.service.KafkaMessageSender;
import com.example.detailsapi.service.MessageSender;
import com.example.detailsapi.service.UsDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import reactor.core.publisher.Mono;

import java.util.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableKafka
class KafkaConsumerConfig {
    public static final String TOPIC_REQUESTS = "ADD_DETAILS_REQUESTS";
    public static final String GROUP_ID = "userDetailsApi";

    private final ObjectMapper objectMapper;
    public static final String TOPIC_REQUESTS_NEWS = "DETAILS_SHOW_REQUESTS";




    @Autowired
    private UsDetailsService usDetailsService;

    @Autowired
    private MessageSender kafkaMessageSender;




    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_REQUESTS)
    public void  addDetailsRequestListener(String msgAsString) {

        Details message = new Details();
        try {

            String str = (objectMapper.readValue(msgAsString,String.class));
            message.takeData(str);


        } catch (Exception ex) {
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }
        Mono<Details> mono = usDetailsService.saveOrUpdate(message.getId(), Mono.just(message));
        mono.subscribe(kafkaMessageSender::send);
        mono.subscribe(System.out::println);



    }
    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_REQUESTS_NEWS)
    public void getDetailsRequestListener(String msgAsString) {


        Long id;
        try {
            id =Long.parseLong(objectMapper.readValue(msgAsString,String.class));

        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }
        Mono<Details> response = usDetailsService.findById(id);
        response.subscribe(kafkaMessageSender::send);

    }



}
