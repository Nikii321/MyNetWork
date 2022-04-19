package com.example.postapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
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
    private String fullName;
    private Long authorId;
    private Instant date;


    public Post(String text, String path, String authorName, Long authorId) {
        this.text = text;
        this.path = path;
        this.authorName = authorName;
        this.authorId = authorId;
        this.date =  Instant.ofEpochMilli(System.currentTimeMillis());
    }
    public String PostToString(){
        String result = "";
        result += "id:"+id+",";
        result+= "AuthorName:"+authorName+",";
        result+="AuthorFullName:"+fullName+",";
        result+="text:"+text+",";
        result += "AuthorId:"+authorId+",";
        result+="path:"+path+",";
        result+="data:"+date+",";
        return result;
    }
    public void takeData(String massage){
        String[] str=massage.split(",");

        for(String tmp:str){
            String[] strings = tmp.split(":");
            switch (strings[0]){
                case "id":
                    this.id = (strings[1].equals("null"))?null:Long.parseLong(strings[1]);
                    break;
                case "AuthorName":
                    this.authorName = strings[1];
                    break;
                case "text":
                    this.text = strings[1];
                    break;
                case "AuthorFullName":
                    this.fullName = strings[1];
                    break;
                case "path":
                    this.path = strings[1];
                    break;
                case "data":
                    this.date = Instant.ofEpochMilli(System.currentTimeMillis());;
                    break;
                case "AuthorId":
                    this.authorId = Long.parseLong(strings[1]);
                    break;
            }
        }

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
