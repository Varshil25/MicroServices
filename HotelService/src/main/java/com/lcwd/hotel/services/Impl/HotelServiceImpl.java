package com.lcwd.hotel.services.Impl;

import com.lcwd.hotel.DTO.HotelDTO;
import com.lcwd.hotel.entities.FileData;
import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.exception.ResourceNotFoundException;
import com.lcwd.hotel.repositories.FileDataRepository;
import com.lcwd.hotel.repositories.HotelRepository;
import com.lcwd.hotel.services.HotelService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final String FOLDER_PATH = "/home/varshil/Desktop/MicroServices/HotelService/src/main/resources/static/images/";

    @Override
    public Hotel create(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel get(String id) {
        return hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel with given id is not found"));
    }

    @Transactional
    @Override
    public Hotel updateHotel(String id, HotelDTO hotelDTO) {
        Optional<Hotel> optionalHotel = hotelRepository.findHotelById(id);
        if (optionalHotel.isPresent()) {
            Hotel existingHotel = optionalHotel.get();
            modelMapper.map(hotelDTO, existingHotel);
            return hotelRepository.save(existingHotel);
        } else {
            throw new ResourceNotFoundException("Hotel with given id is not found" + id);
        }
    }

    @Override
    public void delete(String id) {
        hotelRepository.deleteById(id);
    }

//    Individual for Image
//    @Override
//    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
//        String filePath = FOLDER_PATH + file.getOriginalFilename();
//        FileData fileData = fileDataRepository.save(FileData.builder().filePath(filePath).build()); // meta data for filepath
//        file.transferTo(new File(filePath));
//
//        if (fileData != null) {
//            return "file upload successfully" + filePath;
//        }
//        return null;
//    }

//    @Override
//    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
//        Optional<FileData> imageData = fileDataRepository.findByName(fileName);
//        String filePath = imageData.get().getFilePath();
//        byte[] images = Files.readAllBytes(new File(filePath).toPath());
//        return images;
//    }

    @Override
    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath = FOLDER_PATH + file.getOriginalFilename();
        Hotel hotel = hotelRepository.save(Hotel.builder().filePath(filePath).build()); // meta data for filepath
        file.transferTo(new File(filePath));

        if (hotel != null) {
            return "file upload successfully" + filePath;
        }
        return null;
    }



    @Override
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<Hotel> imageData = hotelRepository.findByName(fileName);
        String filePath = imageData.get().getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

}
