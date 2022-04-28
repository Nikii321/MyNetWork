package com.example.MyNetWork.Repository;

import com.example.MyNetWork.entity.UsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailsRepo extends JpaRepository<UsDetails,Long> {
    @Override
    void delete(UsDetails usDetails);

}