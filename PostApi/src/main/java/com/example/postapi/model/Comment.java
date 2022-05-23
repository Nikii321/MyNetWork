package com.example.postapi.model;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigInteger;

@Table("comment_post")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
public class Comment implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.annotation.Id
    private BigInteger commentId;

    private Long userId;
    private BigInteger postId;
    private String commentText;
    private String commentAuthor;

    public String customToString(){

        String result = "";
        result += "id:="+commentId+",";
        result+= "UserId:="+userId+",";
        result += "PostId:="+postId+",";
        result+= "AuthorName:="+commentAuthor+",";
        result+= "text:="+commentText;
        System.out.println(result);

        return result;
    }
    public void convertData(String massage){
        String[] str=massage.split(",");


        for(String tmp:str){



            String[] strings = tmp.split(":=");
            switch (strings[0]){
                case "id":

                    this.commentId = null;

                    break;
                case "UserId":
                    this.userId = Long.parseLong(strings[1]);
                    break;
                case "PostId":
                    this.postId = BigInteger.valueOf(Long.parseLong(strings[1]));
                    break;

                case "text":
                    this.commentText = strings[1];
                    break;
                case "AuthorName":

                    this.commentAuthor = strings[1];

                    break;
            }
        }
    }

}
