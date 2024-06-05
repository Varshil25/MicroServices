package com.lcwd.hotel.services.Impl;

import com.lcwd.hotel.DTO.HotelDTO;
import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.exception.ResourceNotFoundException;
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
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private ModelMapper modelMapper;

    public final String FOLDER_PATH = "/home/varshil/Desktop/MicroServices/HotelService/src/main/resources/static/images";

>>>>>>> 64e6537 (init')

    @Override
    public Hotel create(Hotel hotel) {
        String hotelId = UUID.randomUUID().toString();
        System.out.println(hotelId);
        hotel.setId(hotelId);
        Hotel savedHotel = hotelRepository.save(hotel);
        String path = "";
        try {
            path = this.uploadImageToFileSystem(hotel.getImage(), savedHotel.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        savedHotel.setFilePath(path);
        return savedHotel;
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
    public String uploadImageToFileSystem(MultipartFile file, String hotelId) throws IOException {
        Hotel hotel = hotelRepository.findHotelById(hotelId).orElseThrow(() -> new ResourceNotFoundException());
        String filePath = FOLDER_PATH + File.separator+ file.getOriginalFilename();
        File dir = new File(FOLDER_PATH);
        if (!dir.exists()) {
            dir.mkdirs(); // Create directory if it doesn't exist
        }
        file.transferTo(new File(filePath));

//        Hotel hotel = new Hotel();
//        hotel.setId(UUID.randomUUID().toString()); // Ensure ID is set if not set in constructor
        hotel.setFilePath(filePath);
        hotelRepository.save(hotel);

        return filePath;
    }

//    @Override
//    public String uploadImageToFileSystem(MultipartFile file, String hotelId) throws IOException {
//        Hotel hotel = hotelRepository.findHotelById(hotelId).orElseThrow(() -> new ResourceNotFoundException());
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//
//        // Create directory if it doesn't exist
//        Path uploadPath = Paths.get(FOLDER_PATH);
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//
//        // Save the image to the server
//        try (InputStream inputStream = file.getInputStream()) {
//            Path filePath = uploadPath.resolve(fileName);
//            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//            hotel.setFilePath("/images/" + fileName); // Assuming images are served from "/images/" URL
//            hotelRepository.save(hotel);
//            return filePath.toString();
//        } catch (IOException e) {
//            throw new IOException("Could not save image: " + fileName, e);
//        }
//    }



    @Override
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<Hotel> hotelData = hotelRepository.findByFilePath(FOLDER_PATH + fileName);
        if (hotelData.isPresent()) {
            Path path = Paths.get(hotelData.get().getFilePath());
            return Files.readAllBytes(path);
        } else {
            throw new IOException("File not found: " + fileName);
        }
    }



}
