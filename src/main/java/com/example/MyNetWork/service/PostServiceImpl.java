package com.example.MyNetWork.service;

import com.example.MyNetWork.entity.User;
import com.example.postapi.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {


    @Autowired
    UserService userService;
    @Autowired
    MessageSender kafkaMessageSender;

    private  HashMap<Long, List<Post>> listHashMap= new HashMap<>();

    public HashMap<Long, List<Post>> getListHashMap() {
        return listHashMap;
    }

    public void addListHashMap( Long id, List<Post> list) {

        listHashMap.put(id,list);
    }

    @Override
    public List<Post> getNews() throws InterruptedException {
        User user = userService.getCurrentUser();

        List<Post> posts = listHashMap.get(user.getId());
        int i=0;
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
    public void sendKafkaListId() {
        User user = userService.getCurrentUser();
        Set<Long> idList = userService.getListIdUser(user);
        kafkaMessageSender.send(idList, user.getId());

    }
}
