package com.example.MyNetWork.service;

import com.example.MyNetWork.Repository.LikeForMongoRepo;
import com.example.MyNetWork.entity.LikeForMongo;
import com.example.MyNetWork.entity.PostForMongo;
import com.example.postapi.model.Post;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
@Service
public class LikeServiceMongoImpl implements LikeServiceMongo{

    @Autowired
    private UserService userService;
    @Autowired
    private MessageSender kafkaMessageSender;
    @Autowired
    private PostServiceMongo postServiceMongo;
    @Autowired
    private LikeForMongoRepo likeForMongoRepo;
    //<-------------------------------------SAVE------------------------------------->
    public void  saveLike(Long id, List<BigInteger> listLikes){

           LikeForMongo like = new LikeForMongo(String.valueOf(id),listLikes);
           likeForMongoRepo.save(like);
    }
    //<-------------------------------------SAVE------------------------------------->



    //<-------------------------------------FIND------------------------------------->
    private List<BigInteger> findLikeInMongo(Long id){
        return likeForMongoRepo.findById(String.valueOf(id)).get().getPostIds();
    }
    private List<BigInteger> getLike(Long id){
        var like = findLikeInMongo(id);
        System.out.println("nice");
        var data = System.currentTimeMillis();
        while (like == null){

            like = findLikeInMongo(id);
            if(System.currentTimeMillis()-data>=1000){
                break;
            }
        }
        return like;
    }
    public CompletableFuture<List<BigInteger>> getUserLike() {
        Long id = userService.getCurrentUser().getId();
        return CompletableFuture.supplyAsync(() -> this.getLike(id));
    }
    //<-----------------------------------FIND------------------------------------->


    //<--------------------------------ADD OR DELETE-------------------------------->

    @SneakyThrows
    public void addOrRemoveLike(BigInteger postId, Long userId, String action, Long authorId){
        List<Post> posts;
        if(authorId==null) {
            posts = postServiceMongo.getNewsMongo(userId).get();
        }
        else {
            posts = postServiceMongo.getMyPostMongo(authorId).get();
        }

        List<BigInteger> like = findLikeInMongo(userId);

        if(action.equals("add") && !like.contains(postId)){
            posts = addLike(posts,like,postId);
        }
        else if(action.equals("delete") && like.contains(postId)) {
            posts = deleteLike(posts,like,postId);
        }
        kafkaMessageSender.sendAddOrDeleteLike(postId, userId,action);
        saveLike(userId,like);
        if(authorId==null) {
            postServiceMongo.saveNews(userId, posts);
        }
        else {
            postServiceMongo.savePost(authorId,posts);
        }
    }


    private List<Post> addLike(List<Post> posts, List<BigInteger> like, BigInteger postId){
        posts = posts.parallelStream().peek(s->{
            if(Objects.equals(s.getId(), postId)){
                s.incrementPlus();
            }
        }).collect(Collectors.toList());
        like.add(postId);
        return posts;

    }
    private List<Post> deleteLike(List<Post> posts, List<BigInteger> like, BigInteger postId){
        posts = posts.parallelStream().peek(s->{
            if(Objects.equals(s.getId(), postId)){
                s.incrementMinus();
            }
        }).collect(Collectors.toList());
        like.remove(postId);
        return posts;
    }
    //<--------------------------------ADD OR DELETE-------------------------------->

}
