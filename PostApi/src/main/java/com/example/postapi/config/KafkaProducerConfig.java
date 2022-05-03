package com.example.postapi.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaProducerConfig {
    public static final String TOPIC_ADD_POST_RESPONSE = "ADD_POST_RESPONSE";
    public static final String TOPIC_RESPONSE_NEWS = "NEWS_SHOW_RESPONSE";
    public static final String TOPIC_DELETE_POST_RESPONSE = "DELETE_POST_RESPONSE";
    public static final String TOPIC_UPDATE_POST_RESPONSE = "UPDATE_SHOW_RESPONSE";
    public static final String TOPIC_GET_AUTHOR_POST_RESPONSE = "POST_GET_AUTHOR_RESPONSE";


    @Bean
    public NewTopic topic2() {
        return TopicBuilder
                .name(TOPIC_ADD_POST_RESPONSE)
                .partitions(3)
                .replicas(2)
                .build();
    }
    @Bean
    public NewTopic topicSnowNewsUser() {
        return TopicBuilder
                .name(TOPIC_RESPONSE_NEWS)
                .partitions(3)
                .replicas(2)
                .build();
    }
    @Bean
    public NewTopic topicDeletePost() {
        return TopicBuilder
                .name(TOPIC_DELETE_POST_RESPONSE)
                .partitions(3)
                .replicas(2)
                .build();
    }
    @Bean
    public NewTopic topicUpdatePost() {
        return TopicBuilder
                .name(TOPIC_UPDATE_POST_RESPONSE)
                .partitions(3)
                .replicas(2)
                .build();
    }
    @Bean
    public NewTopic topicGetPostResponse() {
        return TopicBuilder
                .name(TOPIC_GET_AUTHOR_POST_RESPONSE)
                .partitions(3)
                .replicas(2)
                .build();
    }
}
