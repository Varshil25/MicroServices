package com.lcwd.rating.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document("user_ratings")
public class Rating {
    @Id
    @Indexed(unique = true)
    private String ratingId;
    private String userId;
    private String hotelId;
    private int rating;
    private String feedback;

    
}
