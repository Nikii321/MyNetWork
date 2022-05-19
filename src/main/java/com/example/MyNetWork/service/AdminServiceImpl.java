package com.example.MyNetWork.service;

import com.example.MyNetWork.Repository.UserRepo;
import com.example.MyNetWork.entity.Role;
import com.example.MyNetWork.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepository;
    @PersistenceContext
    private EntityManager em;

    public List<User> allUsersForAdmin() {
        Role role =new Role(1L, "ROLE_USER");
        Set<Role> set = new HashSet<>();
        set.add(role);
        return userRepository.findAllByRoles(set);
    }
    public List<User> allBanForAdmin() {
        Role  role =new Role(4L, "BANED");
        Set<Role> set = new HashSet<>();
        set.add(role);
        return userRepository.findAllByRoles(set);
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
    public List<User> usergtList(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }
}
