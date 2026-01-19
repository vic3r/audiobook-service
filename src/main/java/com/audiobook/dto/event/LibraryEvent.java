package com.audiobook.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryEvent {
    private LibraryEventType eventType;
    private String userId;
    private String audiobookId;
    private String libraryItemId;
    private Integer positionSeconds;
    private Boolean isFavorite;
    private LocalDateTime timestamp;
}
