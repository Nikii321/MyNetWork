package com.example.MyNetWork.service;

import com.example.MyNetWork.entity.User;
import com.example.postapi.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private  boolean isUpdate = false;

    @Autowired
    UserService userService;
    @Autowired
    MessageSender kafkaMessageSender;

    private  HashMap<Long, List<Post>> listHashMap= new HashMap<>();
    private  HashMap<Long, List<Post>> usersPostsHashMap= new HashMap<>();



    @Override
    public void addListHashMap( Long id, List<Post> list) {

        listHashMap.put(id,list);
    }
    @Override
    public void addUsersPostsHashMap( Long id, List<Post> list) {
        usersPostsHashMap.put(id,list);
    }
    @Override
    public void  sendNewPost(Post post){
        isUpdate = true;
        kafkaMessageSender.send(post);
    }

    @Override
    public List<Post> getNews() throws InterruptedException {
        User user = userService.getCurrentUser();

        List<Post> posts = listHashMap.get(user.getId());
        var data = System.currentTimeMillis();
        while (posts == null){

            posts = listHashMap.get(user.getId());
            if(System.currentTimeMillis()-data>=5000){
                break;
            }
        }
        return posts;
    }
    @Override
    public List<Post> getAuthorPost(String username){
        User user = userService.findUserByUsername(username);
        List<Post> posts = usersPostsHashMap.get(user.getId());
        List<Post> newPosts = usersPostsHashMap.get(user.getId());

        var data = System.currentTimeMillis();
        while (posts == null || (isUpdate && newPosts.equals(posts))){

            newPosts = usersPostsHashMap.get(user.getId());
            if(System.currentTimeMillis()-data>=5000){
                return posts;
            }
        }
        return newPosts;


    }

    private List<Long> getPostsId(List<Post> posts){
        return posts.stream().mapToLong(Post::getId).boxed().collect(Collectors.toList());
    }
    public List<Post> isDeleted(Long id, Long PostId){
        List<Long> posts = getPostsId(usersPostsHashMap.get(id));

        while (posts.contains(PostId)){
            kafkaMessageSender.sendGetAuthorPosts(id);
            posts = getPostsId(usersPostsHashMap.get(id));

        }

        return usersPostsHashMap.get(id);
    }

    @Override
    public void sendKafkaListId() {
        User user = userService.getCurrentUser();
        Set<Long> idList = userService.getListIdUser(user);
        kafkaMessageSender.send(idList, user.getId());

    }




}
