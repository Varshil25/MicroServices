package com.lcwd.hotel.controller;

import com.lcwd.hotel.DTO.HotelDTO;
import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    // create
    //  @PreAuthorize("hasAuthority('Admin')")
    @PostMapping
    public ResponseEntity<Hotel> createHotel(@ModelAttribute Hotel hotel) {
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
    }
}
