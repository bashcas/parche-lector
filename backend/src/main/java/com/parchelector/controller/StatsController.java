package com.parchelector.controller;

import com.parchelector.dto.ApiResponse;
import com.parchelector.dto.response.ReadingStatsResponse;
import com.parchelector.model.entity.User;
import com.parchelector.repository.UserRepository;
import com.parchelector.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for reading statistics.
 * 
 * @author Nicolas Arciniegas
 */
@RestController
@RequestMapping("/stats")
@Tag(name = "Statistics", description = "Endpoints for reading statistics and analytics")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get reading statistics for the current user.
     */
    @GetMapping("/me")
    @Operation(summary = "Get my reading statistics", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<ReadingStatsResponse>> getMyStats() {
        try {
            Long currentUserId = getCurrentUserId();
            ReadingStatsResponse stats = statsService.getReadingStats(currentUserId);
            
            ApiResponse<ReadingStatsResponse> response = new ApiResponse<>(
                    "SUCCESS",
                    "Reading statistics retrieved successfully",
                    stats
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<ReadingStatsResponse> response = new ApiResponse<>(
                    "ERROR",
                    "Failed to retrieve statistics: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get reading statistics for a specific user.
     */
    @GetMapping("/users/{userId}")
    @Operation(summary = "Get reading statistics for a user", security = @SecurityRequirement(name = "bearer-jwt"))
    public ResponseEntity<ApiResponse<ReadingStatsResponse>> getUserStats(@PathVariable Long userId) {
        try {
            ReadingStatsResponse stats = statsService.getReadingStats(userId);
            
            ApiResponse<ReadingStatsResponse> response = new ApiResponse<>(
                    "SUCCESS",
                    "Reading statistics retrieved successfully",
                    stats
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<ReadingStatsResponse> response = new ApiResponse<>(
                    "ERROR",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<ReadingStatsResponse> response = new ApiResponse<>(
                    "ERROR",
                    "Failed to retrieve statistics: " + e.getMessage(),
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
