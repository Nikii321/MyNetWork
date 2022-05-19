package com.example.MyNetWork.entity;

import com.example.postapi.model.Post;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;
import java.util.List;

@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document
public class PostForMongo {
    @Id
    private String id;

    private List<Post> postIds;
}
