package com.example.detailsapi.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaProducerConfig {
    public static final String TOPIC_ADD_DETAILS_RESPONSE= "ADD_DETAILS_RESPONSE";
    public static final String TOPIC_DETAILS_SHOW_RESPONSE= "DETAILS_SHOW_RESPONSE";


    @Bean
    public NewTopic topic2() {
        return TopicBuilder
                .name(TOPIC_ADD_DETAILS_RESPONSE)
                .partitions(3)
                .replicas(2)
                .build();
    }
    @Bean
    public NewTopic topicSnowNewsUser() {
        return TopicBuilder
                .name(TOPIC_DETAILS_SHOW_RESPONSE)
                .partitions(3)
                .replicas(2)
                .build();
    }
}

