package com.parchelector.service;

import com.parchelector.dto.request.CreateCommentRequest;
import com.parchelector.dto.response.CommentResponse;
import com.parchelector.model.entity.Review;
import com.parchelector.model.entity.ReviewComment;
import com.parchelector.model.entity.ReviewLike;
import com.parchelector.model.entity.User;
import com.parchelector.repository.ReviewCommentRepository;
import com.parchelector.repository.ReviewLikeRepository;
import com.parchelector.repository.ReviewRepository;
import com.parchelector.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for review interactions (likes, comments).
 * 
 * @author Nicolas Arciniegas
 */
@Service
public class ReviewInteractionService {

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Autowired
    private ReviewCommentRepository reviewCommentRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Like a review.
     */
    @Transactional
    public void likeReview(Long userId, Long reviewId) {
        // Validate review exists and is not deleted
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        
        if (review.getIsDeleted()) {
            throw new IllegalArgumentException("Cannot like a deleted review");
        }

        // Check if already liked
        if (reviewLikeRepository.existsByReviewIdAndUserId(reviewId, userId)) {
            throw new IllegalArgumentException("You have already liked this review");
        }

        // Create like
        ReviewLike reviewLike = new ReviewLike();
        ReviewLike.ReviewLikeId id = new ReviewLike.ReviewLikeId();
        id.setReviewId(reviewId);
        id.setUserId(userId);
        reviewLike.setId(id);

        // Set relationships (needed for @MapsId)
        reviewLike.setReview(review);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        reviewLike.setUser(user);

        reviewLikeRepository.save(reviewLike);
    }

    /**
     * Unlike a review.
     */
    @Transactional
    public void unlikeReview(Long userId, Long reviewId) {
        // Check if like exists
        if (!reviewLikeRepository.existsByReviewIdAndUserId(reviewId, userId)) {
            throw new IllegalArgumentException("You have not liked this review");
        }

        reviewLikeRepository.deleteByReviewIdAndUserId(reviewId, userId);
    }

    /**
     * Add a comment to a review.
     */
    @Transactional
    public CommentResponse addComment(Long userId, Long reviewId, CreateCommentRequest request) {
        // Validate review exists and is not deleted
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        
        if (review.getIsDeleted()) {
            throw new IllegalArgumentException("Cannot comment on a deleted review");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Create comment
        ReviewComment comment = new ReviewComment();
        comment.setReview(review);
        comment.setUser(user);
        comment.setBody(request.getBody());
        comment.setIsDeleted(false);

        comment = reviewCommentRepository.save(comment);

        return mapToCommentResponse(comment);
    }

    /**
     * Get all comments for a review.
     */
    public List<CommentResponse> getReviewComments(Long reviewId) {
        // Validate review exists
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        List<ReviewComment> comments = reviewCommentRepository.findByReviewIdOrderByCreatedAtAsc(reviewId);
        
        return comments.stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());
    }

    /**
     * Delete a comment (soft delete).
     */
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        ReviewComment comment = reviewCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // Check if already deleted
        if (comment.getIsDeleted()) {
            throw new IllegalArgumentException("Comment is already deleted");
        }

        // Check ownership
        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You can only delete your own comments");
        }

        comment.setIsDeleted(true);
        reviewCommentRepository.save(comment);
    }

    /**
     * Check if user has liked a review.
     */
    public boolean hasLikedReview(Long userId, Long reviewId) {
        return reviewLikeRepository.existsByReviewIdAndUserId(reviewId, userId);
    }

    /**
     * Map ReviewComment to CommentResponse.
     */
    private CommentResponse mapToCommentResponse(ReviewComment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getReview().getId(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getUser().getAvatarUrl(),
                comment.getBody(),
                comment.getCreatedAt()
        );
    }
}
