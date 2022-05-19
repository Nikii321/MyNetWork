package com.example.MyNetWork.service;

import com.example.MyNetWork.Repository.RoleRepo;
import com.example.MyNetWork.Repository.UserRepo;
import com.example.MyNetWork.entity.Role;
import com.example.MyNetWork.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepository;
    @Autowired
    private RoleRepo roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private DetailsService detailsService;
    @Autowired
    private ImageService imageService;




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
    public List<User> getPopularUsers(){

        List<User> users = userRepository.findAll();
        users = users.stream().parallel().filter(
                s->!(s.getRoles()
                .contains(new Role(4L,"ROLE_BANED")) || s.getRoles().contains(new Role(0L, "NO_ACTIVE")))
                )
                .sorted(Comparator.comparingInt(s->-1*s.getSubscribers().size()))

                .collect(Collectors.toList());
        return users;
    }




    public Set<Long> getListIdUser(User user){
        Set<Long> res = new HashSet<>();
        for(User tmp:user.getSubscriptions()){
            res.add(tmp.getId());
        }
        return res;
    }

    private void subscription(User userSubscriber, User userAuthor){
        Set<User> usersSubscriptions = userSubscriber.getSubscriptions();
        usersSubscriptions.add(userAuthor);
        userRepository.save(userSubscriber);

    }
    private void subscribe(User userSubscriber, User userAuthor){

        Set<User> usersSubscriber = userSubscriber.getSubscribers();
        usersSubscriber.add(userAuthor);
        userRepository.save(userSubscriber);

    }
    private void unsubscription(User userSubscriber, User userAuthor){
        Set<User> usersSubscriptions = userSubscriber.getSubscriptions();
        usersSubscriptions.remove(userAuthor);
        userRepository.save(userSubscriber);

    }
    private void unsubscribe(User userSubscriber, User userAuthor){

        Set<User> usersSubscriber = userSubscriber.getSubscribers();
        usersSubscriber.remove(userAuthor);
        userRepository.save(userSubscriber);

    }
    public List<User> findUsersListByName(String name){
        User user = findUserByUsername(name);
        if(user!= null){
            return new ArrayList<>(List.of(user));
        }
        List<User> list = userRepository.findAllByUsernameIsLike(name+"%");
        if(list.isEmpty()){
            list = userRepository.findAllByUsernameIsLike("%"+name+"%");
        }
        return list.stream().parallel().filter(
                        s->!(s.getRoles()
                                .contains(new Role(4L,"ROLE_BANED")) || s.getRoles().contains(new Role(0L, "NO_ACTIVE")))
                )
                .sorted(Comparator.comparingInt(s->-1*s.getSubscribers().size()))

                .collect(Collectors.toList());
    }


}