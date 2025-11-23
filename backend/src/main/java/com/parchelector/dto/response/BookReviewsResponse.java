package com.parchelector.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for book reviews with aggregated data.
 * 
 * @author Nicolas Arciniegas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookReviewsResponse {

    private Long bookId;
    private String bookTitle;
    private Double averageRating;
    private int totalReviews;
    private List<ReviewResponse> reviews;
}
