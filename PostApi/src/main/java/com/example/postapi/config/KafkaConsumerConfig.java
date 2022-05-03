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

import static com.example.postapi.config.KafkaProducerConfig.TOPIC_GET_AUTHOR_POST_RESPONSE;
import static com.example.postapi.config.KafkaProducerConfig.TOPIC_RESPONSE_NEWS;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableKafka
class KafkaConsumerConfig {
    public static final String TOPIC_REQUESTS = "ADD_POST_REQUESTS";
    public static final String GROUP_ID = "postApi";

    private final ObjectMapper objectMapper;
    public static final String TOPIC_REQUESTS_NEWS = "NEWS_SHOW_REQUESTS";

    public static final String TOPIC_REQUESTS_POST_DELETE = "POST_DELETE_REQUESTS";
    public static final String TOPIC_REQUESTS_POST_UPDATE = "POST_UPDATE_REQUESTS";
    public static final String TOPIC_REQUESTS_GET_AUTHOR_POST = "POST_GET_AUTHOR_REQUESTS";




    @Autowired
    private PostService postService;

    @Autowired
    private KafkaMessageSender kafkaMessageSender;




    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_REQUESTS)
    public void  RequestListen(String msgAsString) {
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
    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_REQUESTS_NEWS)
    public void  ListNewsForUser(String msgAsString) {
        List<Long> list;
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
            kafkaMessageSender.send(s, finalId, TOPIC_RESPONSE_NEWS);
        });

    }
    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_REQUESTS_POST_DELETE)
    public void  DeletePost(String msgAsString) {
        Long id = 0L;
        try {
            id = Long.parseLong(objectMapper.readValue(msgAsString,String.class));


        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }
        Mono<Void> mono = postService.delete(id);
        mono.subscribe(kafkaMessageSender::send);

    }

    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_REQUESTS_POST_UPDATE)
    public void UpdatePost(String msgAsString) {
        Post message = new Post();

        try {
            message.takeData(objectMapper.readValue(msgAsString,String.class));

        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }
        Mono<Post> mono = postService.update(Mono.just(message),message.getId());

        mono.subscribe(kafkaMessageSender::sendUpdate);
    }

    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_REQUESTS_GET_AUTHOR_POST)
    public void  getAuthorPosts(String msgAsString) {

        msgAsString = msgAsString.replace(" ","");
        Long id = 0L;
        try {
            id = Long.parseLong(objectMapper.readValue(msgAsString,String.class));


        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }
        Flux<Post> response = postService.showByAuthorId(id).sort(Comparator.comparing(Post::getDate).reversed());
        Long finalId = id;
        response.collectList().subscribe(s->{
            kafkaMessageSender.send(s,finalId, TOPIC_GET_AUTHOR_POST_RESPONSE);
        });

    }



}