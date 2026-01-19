package com.audiobook.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AudiobookSeedDto {
    private String title;
    private String author;
    private String description;
    private String narrator;
    private Integer durationMinutes;
    private String price; // JSON has string, we'll convert to BigDecimal
    private String category;
    private String coverImageUrl;
    private Double rating;
    private Integer reviewCount;
}
