package com.example.MyNetWork.Repository;

import com.example.MyNetWork.entity.Role;
import com.example.MyNetWork.entity.UsDetails;
import com.example.MyNetWork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByActivationCode(String code);
    List<User> findAllByRoles(Set<Role> roleSet) ;
}