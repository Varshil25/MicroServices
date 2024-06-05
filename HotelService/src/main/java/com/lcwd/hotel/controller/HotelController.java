package com.lcwd.hotel.controller;

import com.lcwd.hotel.DTO.HotelDTO;
import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.helper.ImageUploadHelper;
import com.lcwd.hotel.services.HotelService;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {


    @Autowired
    private HotelService hotelService;

    @Autowired
    private ImageUploadHelper imageUploadHelper;

    private final Path rootLocation = Paths.get("src/main/resources/static/image");


    // create
    //  @PreAuthorize("hasAuthority('Admin')")
    @PostMapping
    public ResponseEntity<Hotel> create(@ModelAttribute Hotel hotel, @RequestParam("image") MultipartFile image) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.create(hotel, image));
    }


    // get single
    //  @PreAuthorize("hasAuthority('SCOPE_internal')")
    @GetMapping("/{hotelId}")
    public ResponseEntity<Hotel> getSingleHotel(@PathVariable String hotelId) {
        return ResponseEntity.status(HttpStatus.OK).body(hotelService.get(hotelId));
    }

    // get all
    // @PreAuthorize("hasAuthority('SCOPE_internal') || hasAuthority('Admin') ")
    @GetMapping
    public ResponseEntity<List<Hotel>> getAll() {
        return ResponseEntity.ok(hotelService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable String id) {
        hotelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable("id") String id, @RequestBody HotelDTO hotelDTO) {
        Hotel updateHotel = hotelService.updateHotel(id, hotelDTO);
        return ResponseEntity.ok(updateHotel);
    }

<<<<<<< HEAD
    @PostMapping("/upload/{hotelId}")
    public ResponseEntity<?> uploadImageToFileSystem(@PathVariable String hotelId, @RequestParam("image") MultipartFile file) throws IOException {
        String uploadImageResponse = hotelService.uploadImageToFileSystem(file, hotelId);
<<<<<<< HEAD
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/images/").path(file.getOriginalFilename()).toUriString();
        return ResponseEntity.ok(imageUrl);
=======
        String uploadPath = ServletUriComponentsBuilder.fromCurrentContextPath().path("/images/").toUriString();
        return ResponseEntity.ok(uploadPath);
>>>>>>> 64e6537 (init')
    }



    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        byte[] imageData = hotelService.downloadImageFromFileSystem(fileName);
        String downloadPath = ServletUriComponentsBuilder.fromCurrentContextPath().path("/images/").path(fileName).toUriString();
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(imageData);
=======
    @PostMapping("/image")
    public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile image) {
        try {
            if (image.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Request must contain an image");
            }

            if (!image.getContentType().equals("image/jpeg")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Only JPEG content allowed!");
            }

            String filePath = imageUploadHelper.uploadImage(image);
            if (filePath != null) {
                return ResponseEntity.ok(ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(filePath).toUriString());
            }

//            String filePath = imageUploadHelper.uploadImage(image);
//            if (filePath != null) {
//                return ResponseEntity.ok()
//                        .header("Content-Disposition", "attachment; filename=\"" + filePath + "\"")
//                        .body("Image uploaded successfully");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong! Try again");
    }

    // HotelController
//    @GetMapping("/image/{fileName}")
//    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws IOException {
//        try {
//            byte[] images = Files.readAllBytes(new File(fileName).toPath());
//            return ResponseEntity.ok(images);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
//        }
//    }

//    @GetMapping("/image/{fileName}")
//    public ResponseEntity<byte[]> serveImage(@PathVariable String fileName) {
//        try {
//            byte[] images = Files.readAllBytes(new File(fileName).toPath());
//            return ResponseEntity.ok(images);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
//        }
//    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
>>>>>>> 59cb8b3 (init)
    }

}


