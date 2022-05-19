package com.example.MyNetWork.Repository;

import com.example.MyNetWork.entity.LikeForMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface LikeForMongoRepo extends MongoRepository<LikeForMongo,String> {
}
