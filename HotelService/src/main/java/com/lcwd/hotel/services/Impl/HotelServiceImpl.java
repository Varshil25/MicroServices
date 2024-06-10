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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ImageUploadHelper imageUploadHelper;

    private String getFolderPath() throws IOException {
        return new ClassPathResource("static/images/").getFile().getAbsolutePath();
    }

    @Override
    public Hotel create(Hotel hotel, MultipartFile[] images) throws IOException {
        String hotelId = UUID.randomUUID().toString();
        hotel.setId(hotelId);

        List<String> filePaths = new ArrayList<>();

        for (MultipartFile image : images) {
            String filePath = imageUploadHelper.uploadImage(image);
            if (filePath != null) {
                String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/image/")
                        .path(filePath)
                        .toUriString();
                hotel.getFilePath().add(fileUrl);
            }
        }
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAll() {
        List<Hotel> hotels = hotelRepository.findAll();
        if (hotels.isEmpty()) {
            throw new ResourceNotFoundException("No hotels found");
        }
        for (Hotel hotel : hotels) {
            hotel.getFilePath().removeIf(filePath -> Files.exists(Paths.get(filePath)));
        }
        return hotels;
    }

    @Override
    public Hotel get(String id) {
        return hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel with given id is not found"));
    }

    @Transactional
    @Override
    public Hotel updateHotel(String id, String name, String location, String about, MultipartFile[] images) throws IOException {
        Optional<Hotel> optionalHotel = hotelRepository.findHotelById(id);
        if (optionalHotel.isPresent()) {
            Hotel existingHotel = optionalHotel.get();

            // Update name, location, and about
            if (name != null && !name.isEmpty()) {
                existingHotel.setName(name);
            }
            if (location != null && !location.isEmpty()) {
                existingHotel.setLocation(location);
            }
            if (about != null && !about.isEmpty()) {
                existingHotel.setAbout(about);
            }

            // Update images
            if (images != null && images.length > 0) {
                List<String> newFilePaths = new ArrayList<>();
                for (MultipartFile image : images) {
                    String filePath = imageUploadHelper.uploadImage(image);
                    if (filePath != null) {
                        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/image/")
                                .path(filePath)
                                .toUriString();
                        newFilePaths.add(fileUrl);
                    }
                }
                existingHotel.setFilePath(newFilePaths);
            }

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
    public void deleteImage(String id, String[] imagePaths) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel with given id is not found"));
        List<String> filePaths = new ArrayList<String>(hotel.getFilePath());
        for (String imagePath : imagePaths) {
            filePaths.remove(imagePath);
        }

        for (String imagePath : imagePaths) {
            Path path = Paths.get("/home/varshil/Desktop/MicroServices/HotelService/target/classes/static/image/" + imagePath);
            String dbpath="http://localhost:8082/image/"+imagePath;
            List<String> list=hotel.getFilePath();
            list.remove(dbpath);
            hotel.setFilePath(list);

            hotelRepository.save(hotel);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                System.out.println("Error caught: " + e.getMessage());
            }
        }
    }


    @Override
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<Hotel> hotel = hotelRepository.findByName(fileName);
        List<String> filePath = hotel.get().getFilePath();
        byte[] images = Files.readAllBytes(new File(String.valueOf(filePath)).toPath());
        return images;
    }

}
