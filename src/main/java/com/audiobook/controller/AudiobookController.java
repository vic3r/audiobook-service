package com.audiobook.controller;

import com.audiobook.dto.AudiobookDto;
import com.audiobook.service.AudiobookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audiobooks")
@CrossOrigin(origins = "*")
public class AudiobookController {
    
    @Autowired
    private AudiobookService audiobookService;
    
    @GetMapping("/{id}")
    public ResponseEntity<AudiobookDto> getById(@PathVariable String id) {
        AudiobookDto audiobook = audiobookService.getById(id);
        return ResponseEntity.ok(audiobook);
    }
    
    @GetMapping
    public ResponseEntity<Page<AudiobookDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AudiobookDto> audiobooks = audiobookService.getAll(pageable);
        return ResponseEntity.ok(audiobooks);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<AudiobookDto>> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AudiobookDto> audiobooks = audiobookService.search(query, pageable);
        return ResponseEntity.ok(audiobooks);
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<AudiobookDto>> getByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AudiobookDto> audiobooks = audiobookService.getByCategory(category, pageable);
        return ResponseEntity.ok(audiobooks);
    }
    
    @GetMapping("/featured")
    public ResponseEntity<Page<AudiobookDto>> getFeatured(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AudiobookDto> audiobooks = audiobookService.getFeatured(pageable);
        return ResponseEntity.ok(audiobooks);
    }
    
    @GetMapping("/new-releases")
    public ResponseEntity<Page<AudiobookDto>> getNewReleases(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AudiobookDto> audiobooks = audiobookService.getNewReleases(pageable);
        return ResponseEntity.ok(audiobooks);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = audiobookService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
