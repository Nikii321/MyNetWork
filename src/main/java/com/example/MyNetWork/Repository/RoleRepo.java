package com.example.MyNetWork.Repository;

import com.example.MyNetWork.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
}