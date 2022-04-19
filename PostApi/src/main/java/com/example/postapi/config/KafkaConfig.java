package com.example.postapi.config;

import com.example.postapi.model.Post;
import com.example.postapi.repository.PostRepo;
import com.example.postapi.service.PostService;
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
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableKafka
class KafkaConfig {
    public static final String TOPIC_RATE_REQUESTS = "RATE_REQUESTS";
    public static final String GROUP_ID = "postApi";
    private final ObjectMapper objectMapper;

    @Autowired
    PostService postService;
    @Autowired
    PostRepo postRepo;


    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("topic1")
                .partitions(10)
                .replicas(1)
                .build();
    }
    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_RATE_REQUESTS)
    public void  rateRequestListen(String msgAsString) {
        Post message = new Post();
        try {
            message.takeData(objectMapper.readValue(msgAsString,String.class));

        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }
         Mono<Post> mono = postService.save(Mono.just(message));
         mono.subscribe(System.out::println);

        return;


    }


}