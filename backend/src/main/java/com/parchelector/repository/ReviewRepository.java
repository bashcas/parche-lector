package com.parchelector.repository;

import com.parchelector.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Review entity.
 * 
 * @author Nicolas Arciniegas
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.book.id = :bookId AND r.isDeleted = false")
    Optional<Review> findByUserIdAndBookId(Long userId, Long bookId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId AND r.isDeleted = false")
    Double getAverageRatingByBookId(Long bookId);
    
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.isDeleted = false ORDER BY r.createdAt DESC")
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.user.id = :userId AND r.isDeleted = false")
    int countByUserId(Long userId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.user.id = :userId AND r.isDeleted = false")
    Double getAverageRatingByUserId(Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.user.id = :userId AND r.isDeleted = false AND r.rating >= :minRating AND r.rating < :maxRating")
    int countByUserIdAndRatingRange(Long userId, Double minRating, Double maxRating);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.user.id = :userId AND r.isDeleted = false AND EXTRACT(YEAR FROM r.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM r.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE)")
    int countReviewsThisMonth(Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.user.id = :userId AND r.isDeleted = false AND EXTRACT(YEAR FROM r.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE)")
    int countReviewsThisYear(Long userId);
    
    @Query("SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.review.id = :reviewId")
    int countLikesByReviewId(Long reviewId);
    
    @Query("SELECT COUNT(rc) FROM ReviewComment rc WHERE rc.review.id = :reviewId AND rc.isDeleted = false")
    int countCommentsByReviewId(Long reviewId);
    
    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.book WHERE r.book.id = :bookId AND r.isDeleted = false ORDER BY r.createdAt DESC")
    List<Review> findByBookIdOrderByCreatedAtDesc(Long bookId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.book.id = :bookId AND r.isDeleted = false")
    int countByBookId(Long bookId);

    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.book WHERE r.user.id IN :userIds AND r.isDeleted = false ORDER BY r.createdAt DESC")
    List<Review> findByUserIdsOrderByCreatedAtDesc(List<Long> userIds);
}
