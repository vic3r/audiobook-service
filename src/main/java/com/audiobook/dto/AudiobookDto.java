package com.audiobook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudiobookDto {
    private String id;
    private String title;
    private String author;
    private String description;
    private String narrator;
    private Integer durationMinutes;
    private BigDecimal price;
    private String category;
    private String coverImageUrl;
    private String audioFileUrl;
    private Double rating;
    private Integer reviewCount;
    private LocalDateTime publishedDate;
}
