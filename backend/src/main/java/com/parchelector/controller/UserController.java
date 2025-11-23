package com.parchelector.controller;

import com.parchelector.dto.ApiResponse;
import com.parchelector.dto.response.ListResponse;
import com.parchelector.dto.response.ReviewResponse;
import com.parchelector.dto.response.UserProfileResponse;
import com.parchelector.model.entity.User;
import com.parchelector.repository.UserRepository;
import com.parchelector.service.ListService;
import com.parchelector.service.ReviewService;
import com.parchelector.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for public user profiles and content.
 * 
 * @author Nicolas Arciniegas
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints for viewing public user profiles and content")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ListService listService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get public profile of a user.
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Get user public profile", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(@PathVariable Long userId) {
        try {
            UserProfileResponse profile = userService.getUserProfile(userId);
            
            ApiResponse<UserProfileResponse> response = new ApiResponse<>(
                    "SUCCESS",
                    "User profile retrieved successfully",
                    profile
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<UserProfileResponse> response = new ApiResponse<>(
                    "ERROR",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<UserProfileResponse> response = new ApiResponse<>(
                    "ERROR",
                    "Failed to retrieve user profile: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get public lists of a user.
     */
    @GetMapping("/{userId}/lists")
    @Operation(summary = "Get user's public lists", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<List<ListResponse>>> getUserLists(@PathVariable Long userId) {
        try {
            Long currentUserId = getCurrentUserIdOrNull();
            List<ListResponse> lists = listService.getUserPublicLists(userId, currentUserId);
            
            ApiResponse<List<ListResponse>> response = new ApiResponse<>(
                    "SUCCESS",
                    "User lists retrieved successfully",
                    lists
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<ListResponse>> response = new ApiResponse<>(
                    "ERROR",
                    "Failed to retrieve user lists: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get public reviews of a user.
     */
    @GetMapping("/{userId}/reviews")
    @Operation(summary = "Get user's public reviews", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getUserReviews(@PathVariable Long userId) {
        try {
            List<ReviewResponse> reviews = reviewService.getUserReviews(userId);
            
            ApiResponse<List<ReviewResponse>> response = new ApiResponse<>(
                    "SUCCESS",
                    "User reviews retrieved successfully",
                    reviews
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<ReviewResponse>> response = new ApiResponse<>(
                    "ERROR",
                    "Failed to retrieve user reviews: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get current authenticated user ID or null if not authenticated.
     */
    private Long getCurrentUserIdOrNull() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            return user != null ? user.getId() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
