package com.example.MyNetWork.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {
    private  String path = "/Users/nikolajvereschagin/Desktop/RusFaceApp/src/main/webapp/resources/image";

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
}