package com.audiobook.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryEvent {
    private String eventType; // ADDED, POSITION_UPDATED, FAVORITE_TOGGLED
    private String userId;
    private String audiobookId;
    private String libraryItemId;
    private Integer positionSeconds;
    private Boolean isFavorite;
    private LocalDateTime timestamp;
}
