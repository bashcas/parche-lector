package com.parchelector.repository;

import com.parchelector.model.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for ReviewComment entity.
 * 
 * @author Nicolas Arciniegas
 */
@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    @Query("SELECT rc FROM ReviewComment rc JOIN FETCH rc.user WHERE rc.review.id = :reviewId AND rc.isDeleted = false ORDER BY rc.createdAt ASC")
    List<ReviewComment> findByReviewIdOrderByCreatedAtAsc(Long reviewId);
}
