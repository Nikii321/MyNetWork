package com.example.MyNetWork.service;
import com.example.MyNetWork.entity.UsDetails;
import com.example.MyNetWork.entity.User;
import com.example.postapi.model.Post;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;


public interface UserService extends UserDetailsService {
    public UserDetails loadUserByUsername(String username);
    public User findUserById(Long userId);
    public List<User> allUsersForAdmin();
    public List<User> allBanForAdmin();
    public boolean saveUser(User user);
    public boolean deleteUser(Long userId);
    public List<User> usergtList(Long idMin);
    public boolean activateUser(String code);
    public boolean banUser(Long userId);
    public User findUserByUsername(String userName);
    public boolean unbanUser(Long userId);
    public boolean saveUserDetails(User user , UsDetails details);
    public String getCurrentUsername();
    public boolean isSubscribe(String currentUser, String author);
    public User getCurrentUser();
    public boolean isSubscribe(User userSubscriber, User userAuthor);
    public void subscriptionOrUnsubscription(String usernameSubscriber, String user);
    public void sendKafkaListId();
    public List<Post> getNews() throws ExecutionException, InterruptedException;

    void addListHashMap(Long id, List<Post> list);
}