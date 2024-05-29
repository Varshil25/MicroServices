package com.lcwd.rating.services.Impl;

import com.lcwd.rating.DTO.RatingDTO;
import com.lcwd.rating.entities.Rating;
import com.lcwd.rating.repositories.RatingRepository;
import com.lcwd.rating.services.RatingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Rating create(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public List<Rating> getRatingsByUserId(String userId) {
        return ratingRepository.findByUserId(userId);
    }

    @Override
    public List<Rating> getRatingsByHotelId(String hotelId) {
        return ratingRepository.findByHotelId(hotelId);
    }

    @Override
    public Rating updateRating(String ratingId, RatingDTO ratingDTO) {
        Optional<Rating> optionalRating = ratingRepository.findByRatingId(ratingId);
        if (optionalRating.isPresent()) {
            Rating existingRating = optionalRating.get();
            modelMapper.map(ratingDTO, existingRating);
            return ratingRepository.save(existingRating);
        }else {
            throw new RuntimeException("User with this id not found: " + ratingId);
        }
    }
}
