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

    
    public final String UPLOAD_DIR;

    public ImageUploadHelper() throws IOException {
        this.UPLOAD_DIR = new ClassPathResource("static/image/").getFile().getAbsolutePath();
    }

    public String uploadImage(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        Files.copy(multipartFile.getInputStream(), Paths.get(UPLOAD_DIR).resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}

