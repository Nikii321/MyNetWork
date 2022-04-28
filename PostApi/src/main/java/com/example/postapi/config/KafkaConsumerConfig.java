package com.example.postapi.config;

import com.example.postapi.model.Post;
import com.example.postapi.repository.PostRepo;
import com.example.postapi.service.KafkaMessageSender;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableKafka
class KafkaConsumerConfig {
    public static final String TOPIC_RATE_REQUESTS = "ADD_POST_REQUESTS";
    public static final String GROUP_ID = "postApi";

    private final ObjectMapper objectMapper;
    public static final String TOPIC_RATE_REQUESTS_NEWS = "NEWS_SHOW_REQUESTS";




    @Autowired
    private PostService postService;

    @Autowired
    private KafkaMessageSender kafkaMessageSender;



    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("topic1")
                .partitions(1)
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
         mono.subscribe(kafkaMessageSender::send);
    }
    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_RATE_REQUESTS_NEWS)
    public void  rateListNewsForUser(String msgAsString) {
        List<Long> list;
        System.out.println(msgAsString);
        msgAsString = msgAsString.replace(" ","");
        Long id = 0L;
        try {
            list= Arrays.stream(
                    objectMapper.readValue(msgAsString,String.class)
                            .toString().split(",")
                    )
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .collect(Collectors.toList());
            id = list.get(0);

        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }
        Flux<Post> response = postService.showUserNews(Mono.just(list).flatMapMany(Flux::fromIterable).skip(1));
        Long finalId = id;
        response.collectList().subscribe(s->{
            kafkaMessageSender.send(s, finalId);
        });
        response.subscribe(System.out::println);
    }



}