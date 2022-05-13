package com.example.postapi.model;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;


@Table("post")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
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
    private Long countLike;

    public void incrementPlus(){
        countLike = countLike==null?1:countLike+1;
    }
    public void incrementMinus(){
        countLike = (countLike==null||countLike<0)?0:countLike-1;
    }

    public Post(String text, String path, String authorName, Long authorId) {
        this.text = text;
        this.path = path;
        this.authorName = authorName;
        this.authorId = authorId;
        this.date =  Instant.ofEpochMilli(System.currentTimeMillis());
    }
    public String PostToString(){

        String result = "";
        result += "id="+id+",";
        result+= "AuthorName="+authorName+",";
        result+="text="+text+",";
        result += "AuthorId="+authorId+",";
        result+="path="+path+",";
        result+="date="+date+",";
        result+= "countLike="+countLike;
        System.out.println(result);

        return result;
    }
    public void takeData(String massage){
        String[] str=massage.split(",");


        for(String tmp:str){



            String[] strings = tmp.split("=");
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

                case "path":
                    this.path = strings[1];
                    break;
                case "date":


                    this.date = Instant.parse((strings[1]));


                    break;
                case "AuthorId":

                    this.authorId = Long.parseLong(strings[1]);
                    break;
                case "countLike":
                    this.countLike =(strings[1].equals("null"))?null:Long.parseLong(strings[1]);

                    break;
            }
        }
    }

    public String dateFormat(){
        date = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy").localizedBy(Locale.ENGLISH)
                .withZone(ZoneId.systemDefault());
        return formatter.format(date);
    }

}
