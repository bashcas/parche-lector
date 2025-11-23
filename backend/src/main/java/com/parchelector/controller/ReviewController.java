package com.parchelector.controller;

import com.parchelector.dto.ApiResponse;
import com.parchelector.dto.request.CreateReviewRequest;
import com.parchelector.dto.request.UpdateReviewRequest;
import com.parchelector.dto.response.BookReviewsResponse;
import com.parchelector.dto.response.ReviewResponse;
import com.parchelector.model.entity.User;
import com.parchelector.repository.UserRepository;
import com.parchelector.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for review management.
 * 
 * @author Nicolas Arciniegas
 */
@RestController
@RequestMapping("/reviews")
@Tag(name = "Reviews", description = "Endpoints for managing book reviews and ratings")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new review for a book.
     */
    @PostMapping
    @Operation(summary = "Create a new review", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(@Valid @RequestBody CreateReviewRequest request) {
        try {
            Long userId = getCurrentUserId();
            ReviewResponse review = reviewService.createReview(userId, request);
            
            ApiResponse<ReviewResponse> response = new ApiResponse<>(
                    "SUCCESS",
                    "Review created successfully",
                    review
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<ReviewResponse> response = new ApiResponse<>(
                    "ERROR",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<ReviewResponse> response = new ApiResponse<>(
                    "ERROR",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update an existing review.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a review", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReviewRequest request) {
        try {
            Long userId = getCurrentUserId();
            ReviewResponse review = reviewService.updateReview(userId, id, request);
            
            ApiResponse<ReviewResponse> response = new ApiResponse<>(
                    "SUCCESS",
                    "Review updated successfully",
                    review
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<ReviewResponse> response = new ApiResponse<>(
                    "ERROR",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<ReviewResponse> response = new ApiResponse<>(
                    "ERROR",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Delete a review.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a review", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            reviewService.deleteReview(userId, id);
            
            ApiResponse<Void> response = new ApiResponse<>(
                    "SUCCESS",
                    "Review deleted successfully",
                    null
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<Void> response = new ApiResponse<>(
                    "ERROR",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(
                    "ERROR",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get all reviews for a specific book with aggregated rating data.
     */
    @GetMapping("/book/{bookId}")
    @Operation(summary = "Get all reviews for a book", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<BookReviewsResponse>> getBookReviews(@PathVariable Long bookId) {
        try {
            BookReviewsResponse reviews = reviewService.getBookReviews(bookId);
            
            ApiResponse<BookReviewsResponse> response = new ApiResponse<>(
                    "SUCCESS",
                    "Reviews retrieved successfully",
                    reviews
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<BookReviewsResponse> response = new ApiResponse<>(
                    "ERROR",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<BookReviewsResponse> response = new ApiResponse<>(
                    "ERROR",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get user's review for a specific book.
     */
    @GetMapping("/book/{bookId}/my-review")
    @Operation(summary = "Get my review for a book", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<ReviewResponse>> getMyReview(@PathVariable Long bookId) {
        try {
            Long userId = getCurrentUserId();
            ReviewResponse review = reviewService.getUserReviewForBook(userId, bookId);
            
            ApiResponse<ReviewResponse> response = new ApiResponse<>(
                    "SUCCESS",
                    "Review retrieved successfully",
                    review
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<ReviewResponse> response = new ApiResponse<>(
                    "ERROR",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<ReviewResponse> response = new ApiResponse<>(
                    "ERROR",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get current authenticated user ID.
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getId();
    }
}
