package com.example.MyNetWork.service;
import com.example.postapi.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {
    private  String path = "/Users/nikolajvereschagin/Desktop/MyNetWork/src/main/webapp/resources/image/post";

    public boolean upload(MultipartFile file, String path, String fileName){


        String realPath = path + "/" + fileName+".jpg";

        File dest = new File(realPath);

        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        try {
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void delete(String name){
        String realPath = path + "/" +name+".jpg";

        File file = new File(realPath);
        file.delete();
    }
    public void delete(List<Post> posts,Long postId){
        delete(posts.parallelStream().
                filter(s->s.getId()==(postId)).
                collect(Collectors.toList()).get(0).getPath());
        posts.remove(postId);

    }
    public String upload(MultipartFile file){
        String str = randomString();

        String realPath = path + "/" +str+".jpg";

        File dest = new File(realPath);

        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        try {
            file.transferTo(dest);
            return str;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    private String randomString(){
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 12;
        Random random = new Random();

        return  random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}