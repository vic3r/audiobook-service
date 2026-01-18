package com.audiobook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "audiobooks", indexes = {
    @Index(name = "idx_title", columnList = "title"),
    @Index(name = "idx_author", columnList = "author"),
    @Index(name = "idx_category", columnList = "category")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audiobook {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, length = 500)
    private String title;
    
    @Column(nullable = false, length = 200)
    private String author;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private String narrator;
    
    @Column(nullable = false)
    private Integer durationMinutes;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(nullable = false, length = 100)
    private String category;
    
    @Column(length = 500)
    private String coverImageUrl;
    
    @Column(length = 500)
    private String audioFileUrl;
    
    @Column(nullable = false)
    private Double rating = 0.0;
    
    @Column(nullable = false)
    private Integer reviewCount = 0;
    
    private LocalDateTime publishedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "audiobook", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LibraryItem> libraryItems = new HashSet<>();
    
    @OneToMany(mappedBy = "audiobook", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Purchase> purchases = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
