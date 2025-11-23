package com.parchelector.repository;

import com.parchelector.model.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for ReviewLike entity.
 * 
 * @author Nicolas Arciniegas
 */
@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, ReviewLike.ReviewLikeId> {

    boolean existsByReviewIdAndUserId(Long reviewId, Long userId);

    void deleteByReviewIdAndUserId(Long reviewId, Long userId);
}
