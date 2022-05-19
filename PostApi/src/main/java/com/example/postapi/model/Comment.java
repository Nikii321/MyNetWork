package com.example.postapi.model;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigInteger;
import java.time.Instant;

@Table("comment_post")
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = false )
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.annotation.Id
    private BigInteger id;
    @Column(name =  "user_id")
    private Long userId;
    @Column(name =  "post_id")
    private BigInteger postId;
    @Column(name = "comment_text")
    private String text;

    public String customToString(){

        String result = "";
        result += "id:="+id+",";
        result+= "UserId:="+userId+",";
        result += "PostId:="+postId+",";
        result+= "text:="+text;
        System.out.println(result);

        return result;
    }
    public void convertData(String massage){
        String[] str=massage.split(",");


        for(String tmp:str){



            String[] strings = tmp.split(":=");
            switch (strings[0]){
                case "id":
                    this.id = (strings[1].equals("null"))?null:BigInteger.valueOf(Long.parseLong(strings[1]));

                    break;
                case "UserId":
                    this.userId = Long.parseLong(strings[1]);
                    break;
                case "PostId":
                    this.postId = BigInteger.valueOf(Long.parseLong(strings[1]));
                    break;

                case "text":
                    this.text = strings[1];
                    break;
            }
        }
    }

}
