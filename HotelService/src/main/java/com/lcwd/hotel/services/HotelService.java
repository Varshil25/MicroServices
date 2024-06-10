package com.lcwd.hotel.services;

import com.lcwd.hotel.DTO.HotelDTO;
import com.lcwd.hotel.entities.Hotel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HotelService {
    // create
    Hotel create(Hotel hotel, MultipartFile[] images) throws IOException;

    //    getall
    List<Hotel> getAll();

    // get single
    Hotel get(String id);

    // Update Hotel by id
    public Hotel updateHotel(String id, String name, String location, String about, MultipartFile[] images) throws IOException ;

    //    Delete Hotel by id
    void delete(String id);

    //    Delete Image using HotelId and ImagePath
    void deleteImage(String id, String[] imagePaths);


    byte[] downloadImageFromFileSystem(String fileName) throws IOException;

}
