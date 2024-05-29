package com.lcwd.rating.services;

import com.lcwd.rating.DTO.RatingDTO;
import com.lcwd.rating.entities.Rating;

import java.util.List;

public interface RatingService {
    // create
    Rating create(Rating rating);

    // get all rating
    List<Rating> getRatings();

    // get all by user id
    List<Rating> getRatingsByUserId(String userId);

    // get all by hotel
    List<Rating> getRatingsByHotelId(String hotelId);

    // Update rating
    Rating updateRating(String ratingId, RatingDTO ratingDTO);
}
