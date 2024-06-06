package com.lcwd.hotel.helper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class ImageUploadHelper {

    private final String UPLOAD_DIR;
    private int imageCounter = 0;

    public ImageUploadHelper() throws IOException {
        this.UPLOAD_DIR = new ClassPathResource("static/image/").getFile().getAbsolutePath();
    }

    public String uploadImage(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String newFileName = fileName;
        int counter = 0;

        while (new File(UPLOAD_DIR + "/" + newFileName).exists()) {
            counter++;
            newFileName = fileName.substring(0, fileName.lastIndexOf('.')) + String.format("%02d", counter) + fileName.substring(fileName.lastIndexOf('.'));
        }

        Files.copy(multipartFile.getInputStream(), Paths.get(UPLOAD_DIR).resolve(newFileName), StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }
}

