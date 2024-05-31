package com.lcwd.hotel.repositories;

import com.lcwd.hotel.entities.FileData;
import com.lcwd.hotel.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Integer> {
    Optional<FileData> findByName(String fileName);
}
