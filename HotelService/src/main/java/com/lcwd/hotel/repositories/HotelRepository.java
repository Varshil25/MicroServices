package com.lcwd.hotel.repositories;

import com.lcwd.hotel.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, String> {

    @Query("select h from Hotel h where h.id=:id")
    public Optional<Hotel> findHotelById(@Param("id") String id);

    Optional<Hotel> findByName(String fileName);

    @Query("select h from Hotel h where h.id=:id")
    public Object findHotelUsingId(String id);
}
