package com.example.MyNetWork.service;

import com.example.MyNetWork.Repository.RoleRepo;
import com.example.MyNetWork.Repository.UserRepo;
import com.example.MyNetWork.entity.Role;
import com.example.MyNetWork.entity.UsDetails;
import com.example.MyNetWork.entity.User;
import com.example.postapi.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepository;
    @Autowired
    RoleRepo roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    DetailsService detailsService;
    @Autowired
    ImageService imageService;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userName = userRepository.findByUsername(username);

        if (userName == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return  userName;
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }




    public boolean saveUser(User user) {
        User userFromDBName = userRepository.findByUsername(user.getUsername());
        if (userFromDBName != null) {
            return false;
        }

        user.setRoles(Collections.singleton(new Role(0L, "NO_ACTIVE")));

        user.setActivationCode(UUID.randomUUID().toString());
        String massage = String.format(
                "Hello, %s \n"+
                        "Welcome to RusFace. Please,visit next link: http://localhost:8081/activate/%s",
                user.getUsername(),user.getActivationCode()
        );
        if(user.getEmail() != null){
            emailService.sendEmail(user.getEmail(),"Подтверждение почты",massage);
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }



    public boolean activateUser(String code) {
        User user =userRepository.findByActivationCode(code);

        if(user ==null){
            return false;
        }
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setActive(true);
        user.setActivationCode(null);
        deleteUser(user.getId());
        userRepository.save(user);
        return true;
    }


    public User findUserByUsername(String userName){
        return userRepository.findByUsername(userName);
    }




    public String getCurrentUsername(){
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
    public User getCurrentUser(){
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        return findUserByUsername(auth.getName());
    }

    public boolean isSubscribe(String currentUser, String author){
        User userSubscriber = findUserByUsername(currentUser);
        User userAuthor = findUserByUsername(author);
        return (userSubscriber.getSubscriptions().contains(userAuthor));

    }


    public boolean isSubscribe(User userSubscriber, User userAuthor){
        return (userSubscriber.getSubscriptions().contains(userAuthor));

    }
    public void subscriptionOrUnsubscription(String usernameSubscriber, String user){
        if(usernameSubscriber.equals(user)){
            return ;
        }
        User userSubscriber = findUserByUsername(usernameSubscriber);
        User userAuthor = findUserByUsername(user);
        if(isSubscribe(userSubscriber,userAuthor)){
            unsubscription(userSubscriber, userAuthor);
            unsubscribe(userAuthor, userSubscriber);
        }else {
            subscription(userSubscriber, userAuthor);
            subscribe(userAuthor, userSubscriber);
        }
    }




    public Set<Long> getListIdUser(User user){
        Set<Long> res = new HashSet<>();
        for(User tmp:user.getSubscriptions()){
            res.add(tmp.getId());
        }
        return res;
    }

    public void subscription(User userSubscriber, User userAuthor){
        Set<User> usersSubscriptions = userSubscriber.getSubscriptions();
        usersSubscriptions.add(userAuthor);
        userRepository.save(userSubscriber);

    }
    public void subscribe(User userSubscriber, User userAuthor){

        Set<User> usersSubscriber = userSubscriber.getSubscribers();
        usersSubscriber.add(userAuthor);
        userRepository.save(userSubscriber);

    }
    public void unsubscription(User userSubscriber, User userAuthor){
        Set<User> usersSubscriptions = userSubscriber.getSubscriptions();
        usersSubscriptions.remove(userAuthor);
        userRepository.save(userSubscriber);

    }
    public void unsubscribe(User userSubscriber, User userAuthor){

        Set<User> usersSubscriber = userSubscriber.getSubscribers();
        usersSubscriber.remove(userAuthor);
        userRepository.save(userSubscriber);

    }


}