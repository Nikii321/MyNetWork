package com.example.postapi.service;

import com.example.postapi.model.Comment;
import com.example.postapi.repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepo commentRepo;

    @Override
    public Flux<Comment> getComments(BigInteger post) {
        return commentRepo.findAllByPostId(post);
    }

    @Override
    public Mono<Comment> saveComment(BigInteger post_id, Long user_id, String text,String comment_author) {

        return commentRepo.saveComment(post_id, user_id, text,comment_author);
    }

    @Override
    public Mono<Void> deleteComment(BigInteger post_id, Long user_id, String text) {
        return commentRepo.deleteComment(post_id, user_id, text);
    }

    @Override
    public Flux<Comment> findAllByManyPosts(List<BigInteger> posts_id) {
        return commentRepo.findAllByManyPosts(posts_id);
    }
}
