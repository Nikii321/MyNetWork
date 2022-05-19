package com.example.MyNetWork.entity;

import com.example.postapi.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;
@Data
@AllArgsConstructor
@NotNull
@Builder
@ToString
public class PostsAndLike {
    private List<Post> posts;
    private List<BigInteger> likes;
}
