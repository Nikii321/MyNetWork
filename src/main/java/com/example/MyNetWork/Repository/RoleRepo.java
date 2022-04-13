package com.example.MyNetWork.Repository;

import com.example.MyNetWork.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepo extends JpaRepository<Role, Long> {
}