package com.audiobook.repository;

import com.audiobook.model.Audiobook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudiobookRepository extends JpaRepository<Audiobook, String> {
    Page<Audiobook> findByCategory(String category, Pageable pageable);
    Page<Audiobook> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
    
    @Query("SELECT a FROM Audiobook a WHERE " +
           "LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.author) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Audiobook> search(@Param("query") String query, Pageable pageable);
    
    Page<Audiobook> findByOrderByRatingDesc(Pageable pageable);
    Page<Audiobook> findByOrderByPublishedDateDesc(Pageable pageable);
}
