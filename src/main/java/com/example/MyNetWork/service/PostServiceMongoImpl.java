package com.example.MyNetWork.service;

import com.example.MyNetWork.entity.PostForMongo;
import com.example.postapi.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class PostServiceMongoImpl implements PostServiceMongo {



    @Autowired
    private MongoTemplate mongoTemplate;


    //<--------------------------------SAVE-------------------------------->

    private void save(Long id, List<Post> postsList, String collections) {
        PostForMongo postForMongo = new PostForMongo(String.valueOf(id), postsList);
        mongoTemplate.save(postForMongo,collections);
    }


    public void savePost(Long id, List<Post> postList) {
        save(id, postList, "posts");
    }

    public void saveNews(Long id, List<Post> postList) {
        save(id, postList, "news");
    }


    //<--------------------------------SAVE-------------------------------->


    //<--------------------------------FIND-------------------------------->

    private List<Post> findPostInMongo(Long id, String collections) {
        var criteria =Criteria.where("id").is(String.valueOf(id));
        Query query = new Query();
        query.addCriteria(criteria);

        PostForMongo post =  mongoTemplate.findOne(query,PostForMongo.class,collections);


        return post == null ? null : post.getPostIds();
    }


    private List<Post> getPosts(String collections, Long id) {
        List<Post> posts = findPostInMongo(id, collections);
        System.out.println(posts);

        var data = System.currentTimeMillis();
        while (posts == null) {

            posts = findPostInMongo(id, collections);
            if (System.currentTimeMillis() - data >= 1000) {
                break;
            }
        }
        return posts;
    }

    private List<Post> getNews(Long id) {
        return getPosts("news", id);
    }

    private List<Post> getAuthorPost(Long id) {
        return getPosts("posts", id);
    }

    public CompletableFuture<List<Post>> getNewsMongo(Long userId) {
        return CompletableFuture.supplyAsync(() -> this.getNews(userId));
    }

    public CompletableFuture<List<Post>> getMyPostMongo(Long id) {

        return CompletableFuture.supplyAsync(() -> this.getAuthorPost(id));
    }

    //<--------------------------------FIND-------------------------------->


}
