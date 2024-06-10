package com.lcwd.hotel.controller;

import com.lcwd.hotel.DTO.HotelDTO;
import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.helper.ImageUploadHelper;
import com.lcwd.hotel.services.HotelService;
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
import java.util.ArrayList;
import java.util.Arrays;
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
    public ResponseEntity<Hotel> create(@ModelAttribute Hotel hotel, @RequestParam("images") MultipartFile[] images) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.create(hotel, images));
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

//    Delete the hotel using Id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable String id) {
        hotelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable("id") String id, @RequestParam("name") String name,
                                             @RequestParam("location") String location,
                                             @RequestParam("about") String about,
                                             @RequestParam("images") MultipartFile[] images) throws IOException {
        Hotel updatedHotel = hotelService.updateHotel(id, name, location, about, images);
        return ResponseEntity.ok(updatedHotel);
    }



    //    Delete the image using HotelId and imagePath
    @DeleteMapping("/{id}/{imagePaths}")
    public ResponseEntity<Void> deleteImage(@PathVariable String id, @PathVariable String[] imagePaths){
        hotelService.deleteImage(id, imagePaths);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/images")
    public ResponseEntity<String> uploadFile(@RequestParam("images") MultipartFile[] images) {
        try {
            if (Arrays.stream(images).allMatch(MultipartFile::isEmpty)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No images provided");
            }

            for (MultipartFile image : images) {
                if (image.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No images provided");
                }
                if (!image.getContentType().equals("image/jpeg")) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Only JPEG content allowed!");
                }
            }

            StringBuilder urlBuilder = new StringBuilder();

            for (MultipartFile multipartFile : images) {
                try {
                    String filePath = imageUploadHelper.uploadImage(multipartFile);
                    if (filePath != null) {
                        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/image/")
                                .path(filePath)
                                .toUriString();
                        urlBuilder.append(fileUrl).append("\n");
                    }
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong. Try again");
                }
            }

            if (urlBuilder.length() > 0) {
                return ResponseEntity.ok(urlBuilder.toString());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong. Try again");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong. Try again");
        }
    }

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

    }

}