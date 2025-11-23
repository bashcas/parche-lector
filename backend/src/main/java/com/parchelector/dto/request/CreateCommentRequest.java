package com.parchelector.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a comment on a review.
 * 
 * @author Nicolas Arciniegas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {
    
    @NotBlank(message = "Comment body is required")
    @Size(max = 1000, message = "Comment body must not exceed 1000 characters")
    private String body;
}
