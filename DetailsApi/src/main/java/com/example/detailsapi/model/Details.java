package com.example.detailsapi.model;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;

@org.springframework.data.relational.core.mapping.Table("us_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Details {
    @Id
    @org.springframework.data.annotation.Id
    private Long id;

    @DateTimeFormat
    @Column
    private String birthday;

    @Column
    private String work = null;
    @Column
    private String university = null;

    @NumberFormat
    @Column
    private String phone = null;

    @Column
    private String city = null;

    @Column
    private String nameUser;

    @Column
    private String surnameUser;


    @Column
    private String pathImage;

    public String UsDetailsToString(){
        StringBuilder  result= new StringBuilder();
        result.append("id:").append(id).append(",");
        result.append("birthday:").append(birthday).append(",");
        result.append("work:").append(work).append(",");

        result.append("phone:").append(phone).append(",");
        result.append("city:").append(city).append(",");
        result.append("nameUser:").append(nameUser).append(",");

        result.append("surnameUser:").append(surnameUser).append(",");
        result.append("university:").append(university).append(",");
        result.append("pathImage:").append(pathImage);
        return result.toString();
    }
    public void takeData(String massage){
        String[] str=massage.split(",");

        for(String tmp:str){
            String[] strings = tmp.split(":");
            switch (strings[0]){
                case "id":
                    this.id = (strings[1].equals("null"))?null:Long.parseLong(strings[1]);
                    break;
                case "birthday":
                    this.birthday = strings[1];
                    break;
                case "work":
                    this.work = strings[1];
                    break;

                case "phone":
                    this.phone = strings[1];
                    break;
                case "city":
                    this.city = strings[1];
                    break;
                case "nameUser":
                    this.nameUser = strings[1];
                    break;
                case "surnameUser":
                    this.surnameUser = strings[1];
                    break;
                case "pathImage":
                    this.pathImage = strings[1];
                    break;
                case "university":
                    this.university = strings[1];
                    break;
            }
        }

    }




    @Override
    public String toString() {
        return "Details{" +
                "id=" + id +
                ", birthday='" + birthday + '\'' +
                ", work='" + work + '\'' +
                ", university='" + university + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", nameUser='" + nameUser + '\'' +
                ", surnameUser='" + surnameUser + '\'' +
                '}';
    }
}