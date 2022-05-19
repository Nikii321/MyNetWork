package com.example.MyNetWork.Repository;

import com.example.postapi.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

public interface CommentForMongoRepo extends MongoRepository<Comment, BigInteger> {
}
