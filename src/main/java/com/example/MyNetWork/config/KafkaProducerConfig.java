package com.example.MyNetWork.config;


import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    public static final String TOPIC_RATE_REQUESTS = "ADD_POST_REQUESTS";
    public static final String TOPIC_RATE_REQUESTS_NEWS = "NEWS_SHOW_REQUESTS";
    public static final String TOPIC_RATE_REQUESTS_DETAILS_ADD = "ADD_DETAILS_REQUESTS";
    public static final String TOPIC_RATE_REQUESTS_DETAILS = "DETAILS_SHOW_REQUESTS";


    private String bootstrapAddress = "localhost:9092";

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic("baeldung", 1, (short) 1);
    }
    @Bean
    public NewTopic topicNews() { return new NewTopic("baeldung", 2, (short) 1);
    }
    @Bean
    public NewTopic topicDetails() { return new NewTopic("usDetails", 3, (short) 2);
    }

}