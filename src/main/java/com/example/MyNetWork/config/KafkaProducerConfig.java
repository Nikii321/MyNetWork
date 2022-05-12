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


    public static final String TOPIC_REQUESTS_DETAILS_ADD = "ADD_DETAILS_REQUESTS";
    public static final String TOPIC_REQUESTS_DETAILS = "DETAILS_SHOW_REQUESTS";
    public static final String TOPIC_GET_USER_LIKES_REQUESTS ="ADD_OR_DELETE_LIKE";


    public static final String TOPIC_REQUESTS = "ADD_POST_REQUESTS";
    public static final String TOPIC_REQUESTS_NEWS = "NEWS_SHOW_REQUESTS";
    public static final String TOPIC_REQUESTS_POST_DELETE = "POST_DELETE_REQUESTS";
    public static final String TOPIC_REQUESTS_POST_UPDATE = "POST_UPDATE_REQUESTS";
    public static final String TOPIC_REQUESTS_GET_AUTHOR_POST = "POST_GET_AUTHOR_REQUESTS";



    private String bootstrapAddress = "localhost:9092";

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic("postAdd", 1, (short) 1);
    }
    @Bean
    public NewTopic topicNews() { return new NewTopic("news", 2, (short) 1);
    }
    @Bean
    public NewTopic topicDetails() { return new NewTopic("usDetails", 3, (short) 2);
    }
    @Bean
    public NewTopic topicDeletePost() { return new NewTopic("deletePost", 3, (short) 2);
    }
    @Bean
    public NewTopic topicUpdatePost() { return new NewTopic("updatePost", 3, (short) 2);
    }
    @Bean
    public NewTopic topicGetAuthorPosts() { return new NewTopic("updatePost", 3, (short) 2);
    }

    @Bean
    public NewTopic topicAddOrLike() { return new NewTopic("addOrLike", 1, (short) 1);
    }


}