package com.parchelector.service;

import com.parchelector.dto.response.ReadingStatsResponse;
import com.parchelector.dto.response.ReadingStatsResponse.*;
import com.parchelector.model.entity.ReadingStatus;
import com.parchelector.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for reading statistics.
 * 
 * @author Nicolas Arciniegas
 */
@Service
public class StatsService {

    @Autowired
    private ReadingStatusRepository readingStatusRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private LibraryListRepository libraryListRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get comprehensive reading statistics for a user.
     */
    public ReadingStatsResponse getReadingStats(Long userId) {
        // Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Get reading counts
        ReadingCounts counts = getReadingCounts(userId);

        // Get rating statistics
        RatingStats ratingStats = getRatingStats(userId);

        // Get top genres
        List<GenreStats> topGenres = getTopGenres(userId);

        // Get reading trends
        ReadingTrends trends = getReadingTrends(userId);

        return new ReadingStatsResponse(counts, ratingStats, topGenres, trends);
    }

    /**
     * Get reading counts.
     */
    private ReadingCounts getReadingCounts(Long userId) {
        int booksRead = readingStatusRepository.countByUserIdAndStatus(userId, ReadingStatus.ReadingStatusEnum.READ);
        int booksReading = readingStatusRepository.countByUserIdAndStatus(userId, ReadingStatus.ReadingStatusEnum.READING);
        int booksToRead = readingStatusRepository.countByUserIdAndStatus(userId, ReadingStatus.ReadingStatusEnum.WANT_TO_READ);
        
        Long pagesRead = readingStatusRepository.sumPagesReadByUserId(userId);
        int totalPagesRead = (pagesRead != null) ? pagesRead.intValue() : 0;

        int totalReviews = reviewRepository.countByUserId(userId);
        int totalLists = libraryListRepository.findByUserIdOrderByCreatedAtDesc(userId).size();

        return new ReadingCounts(
                booksRead,
                booksReading,
                booksToRead,
                totalPagesRead,
                totalReviews,
                totalLists
        );
    }

    /**
     * Get rating statistics.
     */
    private RatingStats getRatingStats(Long userId) {
        Double averageRating = reviewRepository.getAverageRatingByUserId(userId);
        if (averageRating == null) {
            averageRating = 0.0;
        }

        int totalRatings = reviewRepository.countByUserId(userId);

        // Count books by star rating
        int fiveStarBooks = reviewRepository.countByUserIdAndRatingRange(userId, 4.5, 5.1);
        int fourStarBooks = reviewRepository.countByUserIdAndRatingRange(userId, 3.5, 4.5);
        int threeStarBooks = reviewRepository.countByUserIdAndRatingRange(userId, 2.5, 3.5);
        int twoStarBooks = reviewRepository.countByUserIdAndRatingRange(userId, 1.5, 2.5);
        int oneStarBooks = reviewRepository.countByUserIdAndRatingRange(userId, 0.0, 1.5);

        return new RatingStats(
                averageRating,
                totalRatings,
                fiveStarBooks,
                fourStarBooks,
                threeStarBooks,
                twoStarBooks,
                oneStarBooks
        );
    }

    /**
     * Get top genres read by the user.
     */
    private List<GenreStats> getTopGenres(Long userId) {
        // Get all read books with genres
        List<ReadingStatus> readStatuses = readingStatusRepository.findByUserIdWithBooks(userId);
        
        // Count books by genre
        Map<String, Integer> genreCounts = new HashMap<>();
        int totalBooks = 0;

        for (ReadingStatus rs : readStatuses) {
            if (rs.getStatus() == ReadingStatus.ReadingStatusEnum.READ) {
                totalBooks++;
                for (var genre : rs.getBook().getGenres()) {
                    String genreName = genre.getName();
                    genreCounts.put(genreName, genreCounts.getOrDefault(genreName, 0) + 1);
                }
            }
        }

        // Convert to GenreStats and calculate percentages
        final int finalTotalBooks = totalBooks;
        List<GenreStats> genreStatsList = genreCounts.entrySet().stream()
                .map(entry -> {
                    String genreName = entry.getKey();
                    Integer bookCount = entry.getValue();
                    Double percentage = finalTotalBooks > 0 ? (bookCount * 100.0 / finalTotalBooks) : 0.0;
                    return new GenreStats(genreName, bookCount, Math.round(percentage * 10.0) / 10.0);
                })
                .sorted((a, b) -> b.getBookCount().compareTo(a.getBookCount()))
                .limit(10) // Top 10 genres
                .collect(Collectors.toList());

        return genreStatsList.isEmpty() ? new ArrayList<>() : genreStatsList;
    }

    /**
     * Get reading trends.
     */
    private ReadingTrends getReadingTrends(Long userId) {
        int booksReadThisMonth = readingStatusRepository.countBooksReadThisMonth(userId);
        int booksReadThisYear = readingStatusRepository.countBooksReadThisYear(userId);
        int reviewsThisMonth = reviewRepository.countReviewsThisMonth(userId);
        int reviewsThisYear = reviewRepository.countReviewsThisYear(userId);

        return new ReadingTrends(
                booksReadThisMonth,
                booksReadThisYear,
                reviewsThisMonth,
                reviewsThisYear
        );
    }
}
