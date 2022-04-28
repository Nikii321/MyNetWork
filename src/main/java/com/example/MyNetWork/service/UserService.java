package com.example.MyNetWork.service;
import com.example.MyNetWork.entity.UsDetails;
import com.example.MyNetWork.entity.User;
import com.example.postapi.model.Post;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;


public interface UserService extends UserDetailsService {
    public UserDetails loadUserByUsername(String username);
    public User findUserById(Long userId);
    public boolean saveUser(User user);
    public boolean deleteUser(Long userId);
    public boolean activateUser(String code);
    public User findUserByUsername(String userName);
    public String getCurrentUsername();
    public boolean isSubscribe(String currentUser, String author);
    public User getCurrentUser();
    public boolean isSubscribe(User userSubscriber, User userAuthor);
    public void subscriptionOrUnsubscription(String usernameSubscriber, String user);

    public Set<Long> getListIdUser(User user);
}