package com.example.postapi.model;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;
import java.math.BigInteger;

@Table("like")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.annotation.Id
    private Long id;
    @Column(name =  "user_id")
    private Long idUser;
    @Column(name =  "post_id")
    private BigInteger PostId;
}
