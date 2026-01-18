package com.audiobook.repository;

import com.audiobook.model.LibraryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryItemRepository extends JpaRepository<LibraryItem, String> {
    List<LibraryItem> findByUserIdOrderByLastPlayedAtDesc(String userId);
    Optional<LibraryItem> findByUserIdAndAudiobookId(String userId, String audiobookId);
    List<LibraryItem> findByUserIdAndIsFavoriteTrue(String userId);
}
