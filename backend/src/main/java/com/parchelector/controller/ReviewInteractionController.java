package com.parchelector.controller;

import com.parchelector.dto.ApiResponse;
import com.parchelector.dto.request.CreateCommentRequest;
import com.parchelector.dto.response.CommentResponse;
import com.parchelector.model.entity.User;
import com.parchelector.repository.UserRepository;
import com.parchelector.service.ReviewInteractionService;
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

import java.util.List;

/**
 * REST controller for review interactions (likes, comments).
 * 
 * @author Nicolas Arciniegas
 */
@RestController
@RequestMapping("/reviews")
@Tag(name = "Review Interactions", description = "Endpoints for liking and commenting on reviews")
public class ReviewInteractionController {

    @Autowired
    private ReviewInteractionService reviewInteractionService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Like a review.
     */
    @PostMapping("/{reviewId}/likes")
    @Operation(summary = "Like a review", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<Void>> likeReview(@PathVariable Long reviewId) {
        try {
            Long currentUserId = getCurrentUserId();
            reviewInteractionService.likeReview(currentUserId, reviewId);
            
            ApiResponse<Void> response = new ApiResponse<>(
                    "SUCCESS",
                    "Review liked successfully",
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
                    "Failed to like review: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Unlike a review.
     */
    @DeleteMapping("/{reviewId}/likes")
    @Operation(summary = "Unlike a review", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<Void>> unlikeReview(@PathVariable Long reviewId) {
        try {
            Long currentUserId = getCurrentUserId();
            reviewInteractionService.unlikeReview(currentUserId, reviewId);
            
            ApiResponse<Void> response = new ApiResponse<>(
                    "SUCCESS",
                    "Review unliked successfully",
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
                    "Failed to unlike review: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Check if current user has liked a review.
     */
    @GetMapping("/{reviewId}/likes/status")
    @Operation(summary = "Check if user has liked a review", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<Boolean>> hasLikedReview(@PathVariable Long reviewId) {
        try {
            Long currentUserId = getCurrentUserId();
            boolean hasLiked = reviewInteractionService.hasLikedReview(currentUserId, reviewId);
            
            ApiResponse<Boolean> response = new ApiResponse<>(
                    "SUCCESS",
                    "Like status retrieved successfully",
                    hasLiked
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Boolean> response = new ApiResponse<>(
                    "ERROR",
                    "Failed to check like status: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Add a comment to a review.
     */
    @PostMapping("/{reviewId}/comments")
    @Operation(summary = "Add a comment to a review", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable Long reviewId,
            @Valid @RequestBody CreateCommentRequest request) {
        try {
            Long currentUserId = getCurrentUserId();
            CommentResponse comment = reviewInteractionService.addComment(currentUserId, reviewId, request);
            
            ApiResponse<CommentResponse> response = new ApiResponse<>(
                    "SUCCESS",
                    "Comment added successfully",
                    comment
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<CommentResponse> response = new ApiResponse<>(
                    "ERROR",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<CommentResponse> response = new ApiResponse<>(
                    "ERROR",
                    "Failed to add comment: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get all comments for a review.
     */
    @GetMapping("/{reviewId}/comments")
    @Operation(summary = "Get all comments for a review", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getReviewComments(@PathVariable Long reviewId) {
        try {
            List<CommentResponse> comments = reviewInteractionService.getReviewComments(reviewId);
            
            ApiResponse<List<CommentResponse>> response = new ApiResponse<>(
                    "SUCCESS",
                    "Comments retrieved successfully",
                    comments
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<List<CommentResponse>> response = new ApiResponse<>(
                    "ERROR",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<List<CommentResponse>> response = new ApiResponse<>(
                    "ERROR",
                    "Failed to retrieve comments: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Delete a comment.
     */
    @DeleteMapping("/{reviewId}/comments/{commentId}")
    @Operation(summary = "Delete a comment", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long reviewId,
            @PathVariable Long commentId) {
        try {
            Long currentUserId = getCurrentUserId();
            reviewInteractionService.deleteComment(currentUserId, commentId);
            
            ApiResponse<Void> response = new ApiResponse<>(
                    "SUCCESS",
                    "Comment deleted successfully",
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
                    "Failed to delete comment: " + e.getMessage(),
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
