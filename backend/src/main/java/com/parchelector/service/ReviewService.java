package com.parchelector.service;

import com.parchelector.dto.request.CreateReviewRequest;
import com.parchelector.dto.request.UpdateReviewRequest;
import com.parchelector.dto.response.BookReviewsResponse;
import com.parchelector.dto.response.ReviewResponse;
import com.parchelector.model.entity.Book;
import com.parchelector.model.entity.Review;
import com.parchelector.model.entity.User;
import com.parchelector.repository.BookRepository;
import com.parchelector.repository.ReviewRepository;
import com.parchelector.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for review operations.
 * 
 * @author Nicolas Arciniegas
 */
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Create a new review.
     */
    @Transactional
    public ReviewResponse createReview(Long userId, CreateReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        // Check if user already reviewed this book
        Optional<Review> existingReview = reviewRepository.findByUserIdAndBookId(userId, request.getBookId());
        if (existingReview.isPresent()) {
            throw new IllegalArgumentException("You have already reviewed this book");
        }

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(request.getRating());
        review.setTitle(request.getTitle());
        review.setBody(request.getBody());
        review.setIsDeleted(false);

        Review savedReview = reviewRepository.save(review);
        return mapToReviewResponse(savedReview);
    }

    /**
     * Update an existing review.
     */
    @Transactional
    public ReviewResponse updateReview(Long userId, Long reviewId, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to update this review");
        }

        if (review.getIsDeleted()) {
            throw new IllegalArgumentException("Cannot update a deleted review");
        }

        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }
        if (request.getTitle() != null) {
            review.setTitle(request.getTitle());
        }
        if (request.getBody() != null) {
            review.setBody(request.getBody());
        }

        Review updatedReview = reviewRepository.save(review);
        return mapToReviewResponse(updatedReview);
    }

    /**
     * Delete a review (soft delete).
     */
    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to delete this review");
        }

        review.setIsDeleted(true);
        reviewRepository.save(review);
    }

    /**
     * Get all reviews for a book with aggregated rating data.
     */
    @Transactional(readOnly = true)
    public BookReviewsResponse getBookReviews(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        List<Review> reviews = reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId);
        Double averageRating = reviewRepository.getAverageRatingByBookId(bookId);
        int totalReviews = reviewRepository.countByBookId(bookId);

        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());

        return new BookReviewsResponse(
                bookId,
                book.getTitle(),
                averageRating != null ? averageRating : 0.0,
                totalReviews,
                reviewResponses
        );
    }

    /**
     * Get user's review for a specific book.
     */
    @Transactional(readOnly = true)
    public ReviewResponse getUserReviewForBook(Long userId, Long bookId) {
        Review review = reviewRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        return mapToReviewResponse(review);
    }

    /**
     * Get all public reviews by a user.
     */
    public List<ReviewResponse> getUserReviews(Long userId) {
        // Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Review> reviews = reviewRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return reviews.stream()
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());
    }

    /**
     * Map Review entity to ReviewResponse DTO.
     */
    private ReviewResponse mapToReviewResponse(Review review) {
        int likes = reviewRepository.countLikesByReviewId(review.getId());
        int comments = reviewRepository.countCommentsByReviewId(review.getId());

        return new ReviewResponse(
                review.getId(),
                review.getBook().getId(),
                review.getBook().getTitle(),
                review.getBook().getCoverUrl(),
                review.getUser().getId(),
                review.getUser().getUsername(),
                review.getUser().getAvatarUrl(),
                review.getRating() != null ? review.getRating().doubleValue() : 0.0,
                review.getTitle(),
                review.getBody(),
                review.getCreatedAt().format(DATE_FORMATTER),
                review.getUpdatedAt().format(DATE_FORMATTER),
                likes,
                comments
        );
    }
}
