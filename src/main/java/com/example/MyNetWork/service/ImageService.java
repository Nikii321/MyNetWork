package com.example.MyNetWork.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface ImageService {
    public boolean upload(MultipartFile file, String path, String fileName);
    public String upload(MultipartFile file);
}