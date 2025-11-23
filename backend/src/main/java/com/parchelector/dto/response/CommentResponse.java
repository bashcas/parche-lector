package com.parchelector.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for review comment.
 * 
 * @author Nicolas Arciniegas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    
    private Long id;
    private Long reviewId;
    private Long userId;
    private String username;
    private String userAvatar;
    private String body;
    private LocalDateTime createdAt;
}
