package com.audiobook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryItemDto {
    private String id;
    private AudiobookDto audiobook;
    private Integer lastPositionSeconds;
    private Boolean isCompleted;
    private Boolean isFavorite;
    private LocalDateTime addedAt;
    private LocalDateTime lastPlayedAt;
}
