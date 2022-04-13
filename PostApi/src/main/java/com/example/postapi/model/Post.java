package com.example.postapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;


@Table("post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.annotation.Id
    private Long id;

    private String text;
    private String path;
    private String authorName;
    private Long authorId;
    private Instant date;


    public Post(String text, String path, String authorName, Long authorId) {
        this.text = text;
        this.path = path;
        this.authorName = authorName;
        this.authorId = authorId;
        this.date =  Instant.ofEpochMilli(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", path='" + path + '\'' +
                ", authorName='" + authorName + '\'' +
                ", authorId=" + authorId +
                '}';
    }
}
