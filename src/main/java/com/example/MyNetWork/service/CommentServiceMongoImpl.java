package com.example.MyNetWork.service;

import com.example.MyNetWork.Repository.CommentForMongoRepo;
import com.example.postapi.model.Comment;
import com.example.postapi.model.Post;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.example.MyNetWork.config.KafkaProducerConfig.TOPIC_ADD_COMMENT_REQUESTS;
import static com.example.MyNetWork.config.KafkaProducerConfig.TOPIC_DELETE_COMMENT_REQUESTS;

@Service
public class CommentServiceMongoImpl implements CommentServiceMongo{
    @Autowired
    private CommentForMongoRepo commentForMongoRepo;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MessageSender kafkaMessageSender;

    @SneakyThrows
    public void  saveComment(List<Comment> listComment){

        CompletableFuture.supplyAsync(()-> this.saveAll(listComment)).get();
    }
    private List<Comment> saveAll(List<Comment> listComment){

        listComment = listComment.stream()
                .filter(s->
                        commentForMongoRepo
                                .findAllByUserIdAndAndCommentTextAndPostId(
                                        s.getUserId(),
                                        s.getCommentText(),
                                        s.getPostId()).isEmpty())
                .collect(Collectors.toList());


        return commentForMongoRepo.saveAll(listComment);
    }


    public List<Comment> findFirstsComment(List<Post> posts){
        Query query = new Query();
        List<BigInteger> list = convertListPost(posts);
        var criteria =Criteria.where("postId").in(list);
        query.addCriteria(criteria).with(new Sort(Sort.Direction.DESC,"_id")).limit(3);
        return mongoTemplate.find(query,Comment.class,"comment");
    }

    private List<BigInteger> convertListPost(List<Post> posts){
        return posts.stream().map(Post::getId).collect(Collectors.toList());
    }
    public List<Comment> findPostId(Post post){
        BigInteger bigInteger = post.getId();
        Query query = new Query();
        var criteria =Criteria.where("postId").is(String.valueOf(bigInteger));
        query.addCriteria(criteria);
        return mongoTemplate.find(query,Comment.class,"comment");
    }

    public void saveComment(Comment comment){
        mongoTemplate.save(comment,"comment");
        kafkaMessageSender.send(comment,TOPIC_ADD_COMMENT_REQUESTS);
    }

    public void deleteComment(BigInteger id){
        Comment comment =commentForMongoRepo.findById(id).get();
        commentForMongoRepo.deleteById(id);
        kafkaMessageSender.send(comment,TOPIC_DELETE_COMMENT_REQUESTS);
    }


}
