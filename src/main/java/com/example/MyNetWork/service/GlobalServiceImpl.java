package com.example.MyNetWork.service;

import com.example.MyNetWork.entity.PostsAndLike;
import com.example.MyNetWork.entity.User;
import com.example.postapi.model.Comment;
import com.example.postapi.model.Post;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

import static com.example.MyNetWork.config.KafkaProducerConfig.*;

@Service
public class GlobalServiceImpl implements GlobalService {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageSender kafkaMessageSender;
    @Autowired
    private ImageService imageService;
    @Autowired
    private PostServiceMongo postServiceMongo;
    @Autowired
    private LikeServiceMongo likeServiceMongo;
    @Autowired
    private CommentServiceMongo commentServiceMongo;
    //<------------------------------------------------KAFKA------------------------------------------------>
    @Override
    public void sendKafkaListId() {
        User user = userService.getCurrentUser();
        Set<Long> idList = userService.getListIdUser(user);
        kafkaMessageSender.send(idList, user.getId());
    }
    public void sendNewPost(Post post){
        postServiceMongo.saveOnePost(userService.getCurrentUser().getId());
        kafkaMessageSender.send(post,TOPIC_REQUESTS);
    }
    //<------------------------------------------------KAFKA------------------------------------------------>



    //<------------------------------------------------SAVE------------------------------------------------->
    public void savePost( Long id, List<Post> list){
        postServiceMongo.savePosts(id,list);
    }
    public void  saveNews(Long id,List<Post> post){
        postServiceMongo.saveNews(id,post);
    }
    public void saveLike(Long id, List<BigInteger> list){
        likeServiceMongo.saveLike(id, list);
    }
    public void saveComments(  List<Comment> list){
        commentServiceMongo.saveComment(list);
    }
    //<------------------------------------------------SAVE------------------------------------------------->



    //<------------------------------------------------GET POST AND LIKE------------------------------------------------->
    @SneakyThrows
    public PostsAndLike getNewsWithLike(){
        Long id = userService.getCurrentUser().getId();
        var posts= postServiceMongo.getNewsMongo(id).get();
        var likes = likeServiceMongo.getUserLike().get();
        var comment = commentServiceMongo.findFirstsComment(posts);
        return new PostsAndLike(posts,likes,comment);
    }
    @SneakyThrows
    public PostsAndLike getMyPostWithLike(Long id){
        var posts= postServiceMongo.getMyPostMongo(id).get();
        var likes = likeServiceMongo.getUserLike().get();
        var comment = commentServiceMongo.findFirstsComment(posts);
        return new PostsAndLike(posts,likes,comment);

    }
    //<------------------------------------------------GET POST AND LIKE------------------------------------------------->


    @SneakyThrows
    public List<Post> delete(Long id, BigInteger postId){
        kafkaMessageSender.sendDeletePostRequest(postId);
        List<Post> posts = postServiceMongo.getMyPostMongo(id).get();
        imageService.delete(posts, postId);
        var newPost =  deletePostById(posts,postId);
        postServiceMongo.savePosts(id, newPost);
        return newPost;
    }
    private Post getPostById(List<Post> posts,BigInteger postId){
        return  posts.parallelStream().filter(s->s.getId().equals(postId)).findFirst().get();
    }
    private List<Post> deletePostById(List<Post> posts,BigInteger postId){
        posts.remove(getPostById(posts,postId));
        return posts;
    }

    public void addOrRemoveLike(BigInteger postId,Long userId,String action,Long authorId){
        likeServiceMongo.addOrRemoveLike(postId,userId,action,authorId);
    }
    public void saveComment(String text,BigInteger postId){
        User user =userService.getCurrentUser();
        saveComment(Comment.builder()
                .commentText(text)
                .postId(postId)
                .userId(user.getId())
                .commentAuthor(user.getUsername()).build());

    }

    private void saveComment(Comment comment){
        commentServiceMongo.saveComment(comment);

    }
    public void deleteComment(BigInteger id){
        commentServiceMongo.deleteComment(id);
    }



}