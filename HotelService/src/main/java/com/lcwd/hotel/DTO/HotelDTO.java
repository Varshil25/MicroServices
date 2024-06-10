package com.lcwd.hotel.DTO;

import lombok.Data;

import java.util.List;

@Data
public class HotelDTO {
    private String name;
    private String location;
    private String about;
    private List<String> filePath;
}
