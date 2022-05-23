package com.example.MyNetWork.Repository;

import com.example.postapi.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;
import java.util.List;

public interface CommentForMongoRepo extends MongoRepository<Comment, BigInteger> {
    List<Comment> findAllByUserIdAndAndCommentTextAndPostId(Long userId,String text, BigInteger postId);
}
