package com.example.MyNetWork.Repository;

import com.example.MyNetWork.entity.UsDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DetailsRepo extends JpaRepository<UsDetails,Long> {
    @Override
    void delete(UsDetails usDetails);

}