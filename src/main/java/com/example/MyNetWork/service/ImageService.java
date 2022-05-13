package com.example.MyNetWork.service;


import com.example.postapi.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImageService {
    public boolean upload(MultipartFile file, String path, String fileName);
    public String upload(MultipartFile file);
    public void delete(String name);
    public void delete(List<Post> posts, Long postId);
}