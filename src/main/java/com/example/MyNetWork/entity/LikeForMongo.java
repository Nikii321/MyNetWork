package com.example.MyNetWork.entity;

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
public class LikeForMongo {
    @Id
    private String id;

    private List<BigInteger> postIds;
}
