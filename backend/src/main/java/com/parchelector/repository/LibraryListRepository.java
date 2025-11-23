package com.parchelector.repository;

import com.parchelector.model.entity.LibraryList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for LibraryList entity.
 * 
 * @author Nicolas Arciniegas
 */
@Repository
public interface LibraryListRepository extends JpaRepository<LibraryList, Long> {
    
    List<LibraryList> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT COUNT(lb) FROM ListBook lb WHERE lb.list.id = :listId")
    int countBooksByListId(Long listId);

    @Query("SELECT l FROM LibraryList l WHERE l.user.id IN :userIds ORDER BY l.createdAt DESC")
    List<LibraryList> findByUserIdsOrderByCreatedAtDesc(List<Long> userIds);

    @Query("SELECT l FROM LibraryList l WHERE l.user.id = :userId AND (l.visibility = 'PUBLIC' OR (l.visibility = 'FOLLOWERS_ONLY' AND :isFollower = true)) ORDER BY l.createdAt DESC")
    List<LibraryList> findPublicListsByUserId(Long userId, boolean isFollower);
}
