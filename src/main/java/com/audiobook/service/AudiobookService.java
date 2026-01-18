package com.audiobook.service;

import com.audiobook.dto.AudiobookDto;
import com.audiobook.model.Audiobook;
import com.audiobook.repository.AudiobookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AudiobookService {
    
    @Autowired
    private AudiobookRepository audiobookRepository;
    
    @Cacheable(value = "audiobooks", key = "#id")
    public AudiobookDto getById(String id) {
        Audiobook audiobook = audiobookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Audiobook not found"));
        return convertToDto(audiobook);
    }
    
    public Page<AudiobookDto> getAll(Pageable pageable) {
        return audiobookRepository.findAll(pageable)
            .map(this::convertToDto);
    }
    
    public Page<AudiobookDto> search(String query, Pageable pageable) {
        return audiobookRepository.search(query, pageable)
            .map(this::convertToDto);
    }
    
    public Page<AudiobookDto> getByCategory(String category, Pageable pageable) {
        return audiobookRepository.findByCategory(category, pageable)
            .map(this::convertToDto);
    }
    
    public Page<AudiobookDto> getFeatured(Pageable pageable) {
        return audiobookRepository.findByOrderByRatingDesc(pageable)
            .map(this::convertToDto);
    }
    
    public Page<AudiobookDto> getNewReleases(Pageable pageable) {
        return audiobookRepository.findByOrderByPublishedDateDesc(pageable)
            .map(this::convertToDto);
    }
    
    public List<String> getAllCategories() {
        return audiobookRepository.findAll().stream()
            .map(Audiobook::getCategory)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }
    
    private AudiobookDto convertToDto(Audiobook audiobook) {
        return new AudiobookDto(
            audiobook.getId(),
            audiobook.getTitle(),
            audiobook.getAuthor(),
            audiobook.getDescription(),
            audiobook.getNarrator(),
            audiobook.getDurationMinutes(),
            audiobook.getPrice(),
            audiobook.getCategory(),
            audiobook.getCoverImageUrl(),
            audiobook.getAudioFileUrl(),
            audiobook.getRating(),
            audiobook.getReviewCount(),
            audiobook.getPublishedDate()
        );
    }
}
