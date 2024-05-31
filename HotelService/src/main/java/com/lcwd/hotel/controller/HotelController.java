package com.lcwd.hotel.controller;

import com.lcwd.hotel.DTO.HotelDTO;
import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @Value("${project.image}")
    private String path;

    // create
    //  @PreAuthorize("hasAuthority('Admin')")
    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.create(hotel));
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

    @PostMapping("/fileSystem")
    public ResponseEntity<?> uploadImageTOFileSystem(@RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = hotelService.uploadImageToFileSystem(file);
        return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
    }

    @GetMapping("/fileSyste/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        byte[] imageDate = hotelService.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("/image/png"))
                .body(imageDate);
    }
}
