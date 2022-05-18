package com.example.postapi.config;

import com.example.postapi.model.Comment;
import com.example.postapi.model.Like;
import com.example.postapi.model.Post;
import com.example.postapi.service.CommentService;
import com.example.postapi.service.KafkaMessageSender;
import com.example.postapi.service.LikeService;
import com.example.postapi.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.postapi.config.KafkaProducerConfig.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableKafka
class KafkaConsumerConfig {
    public static final String TOPIC_REQUESTS = "ADD_POST_REQUESTS";
    public static final String GROUP_ID = "postApi";


    private final ObjectMapper objectMapper;
    public static final String TOPIC_REQUESTS_NEWS_REQUESTS = "NEWS_SHOW_REQUESTS";
    public static final String TOPIC_REQUESTS_POST_DELETE_REQUESTS = "POST_DELETE_REQUESTS";
    public static final String TOPIC_REQUESTS_POST_UPDATE_REQUESTS = "POST_UPDATE_REQUESTS";
    public static final String TOPIC_REQUESTS_GET_AUTHOR_POST_REQUESTS = "POST_GET_AUTHOR_REQUESTS";
    public static final String TOPIC_ADD_OR_DELETE_LIKE_REQUESTS ="ADD_OR_DELETE_LIKE";
    public static final String TOPIC_ADD_COMMENT_REQUESTS = "TOPIC_ADD_COMMENT_REQUESTS";


    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private KafkaMessageSender kafkaMessageSender;

    @Autowired
    private LikeService likeService;




    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_REQUESTS)
    public void  RequestListen(String msgAsString) {
        Post message = new Post();
        try {
            String str = (objectMapper.readValue(msgAsString,String.class));

            message.convertData(str);

        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }


        Mono<Post> mono = postService.save(Mono.just(message));
        mono.subscribe(kafkaMessageSender::send);
    }
    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_REQUESTS_NEWS_REQUESTS)
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
        Flux<Post> response = postService.showUserNews(Mono.just(list).flatMapMany(Flux::fromIterable).skip(1)).switchIfEmpty(postService.getPopularPosts(id));
        Flux<Long> responseForLike = likeService.findAllByUserId(id);

        Long finalId = id;



        response.collectList().subscribe(s->{
            kafkaMessageSender.send(s, finalId, TOPIC_RESPONSE_NEWS);
        });
        Flux<Comment> responseForComment = response.map(Post::getId).collectList().flatMapMany(s->commentService.findAllByManyPosts(s));
        responseForComment.collectList().subscribe(s->{
            kafkaMessageSender.send(s, finalId, TOPIC_GET_COMMENT_RESPONSE);
        });
        responseForLike.collectList().subscribe(s->{
            kafkaMessageSender.sendForLike(s,finalId, TOPIC_GET_USER_LIKES_RESPONSE);
        });


    }

    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_REQUESTS_POST_DELETE_REQUESTS)
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

    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_REQUESTS_POST_UPDATE_REQUESTS)
    public void UpdatePost(String msgAsString) {
        Post message = new Post();

        try {
            message.convertData(objectMapper.readValue(msgAsString,String.class));

        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }
        Mono<Post> mono = postService.update(Mono.just(message),message.getId());

        mono.subscribe(kafkaMessageSender::sendUpdate);
    }

    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_REQUESTS_GET_AUTHOR_POST_REQUESTS)
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
        Flux<Long> responseForLike = likeService.findAllByUserId(id);



        response.collectList().subscribe(s->{
            kafkaMessageSender.send(s,finalId, TOPIC_GET_AUTHOR_POST_RESPONSE);
        });
        Flux<Comment> responseForComment = response.map(Post::getId).collectList().flatMapMany(s->commentService.findAllByManyPosts(s));
        responseForComment.collectList().subscribe(s->{
            kafkaMessageSender.send(s, finalId, TOPIC_GET_COMMENT_RESPONSE);
        });        responseForLike.collectList().subscribe(s->{
            kafkaMessageSender.sendForLike(s,finalId, TOPIC_GET_USER_LIKES_RESPONSE);
        });
    }

    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_ADD_OR_DELETE_LIKE_REQUESTS)
    public void  addOrDeleteLike(String msgAsString){
        String[] tmp;
        try {
            tmp = (objectMapper.readValue(msgAsString,String.class)).split(" ; ");


        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }
        var userId =Long.parseLong(tmp[0]);
        var postId =Long.parseLong(tmp[1]);
        var action =(tmp[2]);
        if(action.equals("add")){
            Mono<Like> mono = likeService.saveLike(postId,userId);
            Mono<Post> postMono = postService.incrementPlus(postId);
            postMono.subscribe();
            mono.subscribe();
        }
        else {
            Mono<Void> mono  = likeService.deleteLike(postId,userId);
            Mono<Post> postMono = postService.incrementMinus(postId);
            Mono<Like> likeMono = mono.flatMap(s->{
                return likeService.findByPostIdAndIdUser(postId,userId);});
            postMono.subscribe();
            likeMono.subscribe();
        }
    }
    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_ADD_COMMENT_REQUESTS)
    public void AddComment(String msgAsString){
        Comment message = new Comment();
        try {
            String str = (objectMapper.readValue(msgAsString,String.class));

            message.convertData(str);

        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }


        Mono<Comment> mono = commentService.saveComment(message.getPostId(),message.getUserId(),message.getText());
        mono.subscribe();
    }

    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_ADD_COMMENT_REQUESTS)
    public void deleteCommit(String msgAsString){
        Comment message = new Comment();
        try {
            String str = (objectMapper.readValue(msgAsString,String.class));

            message.convertData(str);

        } catch (Exception ex) {
            log.error("can't parse message:{}", msgAsString, ex);
            throw new RuntimeException("can't parse message:" + msgAsString, ex);
        }


        Mono<Void> mono = commentService.deleteComment(message.getPostId(),message.getUserId(),message.getText());
        mono.subscribe();
    }

}