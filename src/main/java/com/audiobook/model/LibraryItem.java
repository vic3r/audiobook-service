package com.audiobook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "library_items", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_audiobook_id", columnList = "audiobook_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audiobook_id", nullable = false)
    private Audiobook audiobook;
    
    @Column(nullable = false)
    private Integer lastPositionSeconds = 0;
    
    @Column(nullable = false)
    private Boolean isCompleted = false;
    
    @Column(nullable = false)
    private Boolean isFavorite = false;
    
    private LocalDateTime addedAt;
    private LocalDateTime lastPlayedAt;
    
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}
