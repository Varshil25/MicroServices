package com.lcwd.hotel.services.Impl;

import com.lcwd.hotel.DTO.HotelDTO;
import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.exception.ResourceNotFoundException;
import com.lcwd.hotel.helper.ImageUploadHelper;
import com.lcwd.hotel.repositories.HotelRepository;
import com.lcwd.hotel.services.HotelService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

<<<<<<< HEAD

    @Autowired
    private ModelMapper modelMapper;

    public final String FOLDER_PATH = new ClassPathResource("static/images/").getFile().getAbsolutePath();

    public HotelServiceImpl() throws IOException {
    }
=======
    @Autowired
    private ImageUploadHelper imageUploadHelper;

    @Autowired
    private ModelMapper modelMapper;


<<<<<<< HEAD
>>>>>>> 64e6537 (init')

    @Override
    public Hotel create(Hotel hotel) {
=======
    public Hotel create(Hotel hotel, MultipartFile image) throws IOException {
>>>>>>> 59cb8b3 (init)
        String hotelId = UUID.randomUUID().toString();
        hotel.setId(hotelId);
        String filePath = imageUploadHelper.uploadImage(image);
        hotel.setFilePath("localhost:8082/image/" + filePath);
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

    @Override
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
       Optional<Hotel> hotel = hotelRepository.findByName(fileName);
       String filePath = hotel.get().getFilePath();
       byte[] images = Files.readAllBytes(new File(filePath).toPath());
       return images;
    }




}
