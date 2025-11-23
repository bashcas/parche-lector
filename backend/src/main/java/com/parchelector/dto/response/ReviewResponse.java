package com.parchelector.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for review response.
 * 
 * @author Nicolas Arciniegas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private String bookCover;
    private Long userId;
    private String username;
    private String userAvatar;
    private Double rating;
    private String title;
    private String body;
    private String createdAt;
    private String updatedAt;
    private int likes;
    private int comments;
}
