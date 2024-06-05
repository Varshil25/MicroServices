package com.lcwd.hotel.services;

import com.lcwd.hotel.DTO.HotelDTO;
import com.lcwd.hotel.entities.Hotel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HotelService {
    // create
    Hotel create(Hotel hotel, MultipartFile image) throws IOException;

//    getall
    List<Hotel> getAll();

    // get single
    Hotel get(String id);

    // Update Hotel by id
    Hotel updateHotel(String id, HotelDTO hotelDTO);

//    Delete Hotel by id
    void delete(String id);

    byte[] downloadImageFromFileSystem(String fileName) throws IOException;

}
