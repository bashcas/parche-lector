package com.parchelector.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for reading statistics.
 * 
 * @author Nicolas Arciniegas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingStatsResponse {
    
    private ReadingCounts counts;
    private RatingStats ratingStats;
    private List<GenreStats> topGenres;
    private ReadingTrends trends;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadingCounts {
        private Integer totalBooksRead;
        private Integer totalBooksReading;
        private Integer totalBooksToRead;
        private Integer totalPagesRead;
        private Integer totalReviews;
        private Integer totalLists;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingStats {
        private Double averageRating;
        private Integer totalRatings;
        private Integer fiveStarBooks;
        private Integer fourStarBooks;
        private Integer threeStarBooks;
        private Integer twoStarBooks;
        private Integer oneStarBooks;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenreStats {
        private String genreName;
        private Integer bookCount;
        private Double percentage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadingTrends {
        private Integer booksReadThisMonth;
        private Integer booksReadThisYear;
        private Integer reviewsThisMonth;
        private Integer reviewsThisYear;
    }
}
