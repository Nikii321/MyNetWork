package com.example.MyNetWork.entity;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "us_details")
public class UsDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
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
    @OneToOne
    User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getSurnameUser() {
        return surnameUser;
    }

    public void setSurnameUser(String surnameUser) {
        this.surnameUser = surnameUser;
    }

    public UsDetails() {
    }

    public UsDetails(String birthday, String work, String university, String phone, String city, String nameUser, String surnameUser) {
        this.birthday = birthday;
        this.work = work;
        this.university = university;
        this.phone = phone;
        this.city = city;
        this.nameUser = nameUser;
        this.surnameUser = surnameUser;
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