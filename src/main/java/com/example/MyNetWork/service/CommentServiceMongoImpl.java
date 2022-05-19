package com.example.MyNetWork.service;

import com.example.MyNetWork.Repository.CommentForMongoRepo;
import com.example.MyNetWork.entity.LikeForMongo;
import com.example.postapi.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
@Service
public class CommentServiceMongoImpl implements CommentServiceMongo{
    @Autowired
    private CommentForMongoRepo commentForMongoRepo;

    public void  saveComment(List<Comment> listComment){
        commentForMongoRepo.saveAll(listComment);

    }
    private Comment findCommentInMongo(BigInteger id) {
        return commentForMongoRepo.findById(id).get();
    }

}
