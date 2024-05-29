package com.lcwd.rating.DTO;

import lombok.Data;

@Data
public class RatingDTO {
    private String userId;
    private String hotelId;
    private int rating;
    private String feedback;
}
