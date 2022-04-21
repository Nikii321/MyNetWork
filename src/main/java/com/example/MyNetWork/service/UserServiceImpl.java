package com.example.MyNetWork.service;

import com.example.MyNetWork.Repository.RoleRepo;
import com.example.MyNetWork.Repository.UserRepo;
import com.example.MyNetWork.config.KafkaConsumerConfig;
import com.example.MyNetWork.entity.Role;
import com.example.MyNetWork.entity.UsDetails;
import com.example.MyNetWork.entity.User;
import com.example.postapi.model.Post;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Service
public class UserServiceImpl implements UserService {
    @PersistenceContext
    private EntityManager em;
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

    private HashMap<Long, List<Post>> listHashMap= new HashMap<>();

    public HashMap<Long, List<Post>> getListHashMap() {
        return listHashMap;
    }

    public void addListHashMap( Long id, List<Post> list) {

        listHashMap.put(id,list);
    }
    @Autowired
    MessageSender kafkaMessageSender;

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

    public List<User> allUsersForAdmin() {
        Role  role =new Role(1L, "ROLE_USER");
        Set<Role> set = new HashSet<>();
        set.add(role);
        User user = getCurrentUser();
        return userRepository.findAllByRoles(set);
    }
    public List<User> allBanForAdmin() {
        Role  role =new Role(4L, "BANED");
        Set<Role> set = new HashSet<>();
        set.add(role);
        return userRepository.findAllByRoles(set);
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

    public List<User> usergtList(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
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

    public boolean banUser(Long userId){
        if (userRepository.findById(userId).isPresent()) {
            User user = userRepository.findById(userId).get();
            Set<Role> roles = new HashSet<>();
            roles.add(new Role(4L,"ROLE_BANED"));
            user.setRoles(roles);
            userRepository.save(user);
            return true;
        }
        return false;
    }
    public User findUserByUsername(String userName){
        return userRepository.findByUsername(userName);
    }

    public boolean unbanUser(Long userId){
        if (userRepository.findById(userId).isPresent()) {
            User user = userRepository.findById(userId).get();
            Set<Role> roles = new HashSet<>();
            roles.add(new Role(1L,"ROLE_USER"));
            user.setRoles(roles);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean saveUserDetails(User user , UsDetails details){
        UsDetails usDetails = user.getUsDetails();
        detailsService.save(details);
        user.setUsDetails(details);
        userRepository.save(user);

        return true;

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

    @Override
    public void sendKafkaListId() {
        User user = getCurrentUser();
        Set<Long> idList = getListIdUser(user);
        kafkaMessageSender.send(idList, user.getId());

    }
    public List<Post> getNews() throws InterruptedException {
        User user = getCurrentUser();
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


    private Set<Long> getListIdUser(User user){
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