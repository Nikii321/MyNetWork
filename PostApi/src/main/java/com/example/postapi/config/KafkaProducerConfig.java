package com.example.postapi.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaProducerConfig {
    public static final String TOPIC_RATE_RESPONSE = "ADD_POST_RESPONSE";
    public static final String TOPIC_RATE_RESPONSE_NEWS = "NEWS_SHOW_RESPONSE";


    @Bean
    public NewTopic topic2() {
        return TopicBuilder
                .name(TOPIC_RATE_RESPONSE)
                .partitions(3)
                .replicas(2)
                .build();
    }
    @Bean
    public NewTopic topicSnowNewsUser() {
        return TopicBuilder
                .name(TOPIC_RATE_RESPONSE_NEWS)
                .partitions(3)
                .replicas(2)
                .build();
    }
}
