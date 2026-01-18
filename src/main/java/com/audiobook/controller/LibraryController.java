package com.audiobook.controller;

import com.audiobook.dto.LibraryItemDto;
import com.audiobook.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@CrossOrigin(origins = "*")
public class LibraryController {
    
    @Autowired
    private LibraryService libraryService;
    
    @GetMapping
    public ResponseEntity<List<LibraryItemDto>> getUserLibrary(Authentication authentication) {
        String userId = authentication.getName();
        List<LibraryItemDto> library = libraryService.getUserLibrary(userId);
        return ResponseEntity.ok(library);
    }
    
    @GetMapping("/favorites")
    public ResponseEntity<List<LibraryItemDto>> getFavorites(Authentication authentication) {
        String userId = authentication.getName();
        List<LibraryItemDto> favorites = libraryService.getFavorites(userId);
        return ResponseEntity.ok(favorites);
    }
    
    @PostMapping("/{audiobookId}")
    public ResponseEntity<LibraryItemDto> addToLibrary(
            @PathVariable String audiobookId,
            Authentication authentication) {
        String userId = authentication.getName();
        LibraryItemDto item = libraryService.addToLibrary(userId, audiobookId);
        return ResponseEntity.ok(item);
    }
    
    @PutMapping("/{libraryItemId}/position")
    public ResponseEntity<LibraryItemDto> updatePlaybackPosition(
            @PathVariable String libraryItemId,
            @RequestParam Integer positionSeconds,
            Authentication authentication) {
        String userId = authentication.getName();
        LibraryItemDto item = libraryService.updatePlaybackPosition(userId, libraryItemId, positionSeconds);
        return ResponseEntity.ok(item);
    }
    
    @PutMapping("/{libraryItemId}/favorite")
    public ResponseEntity<LibraryItemDto> toggleFavorite(
            @PathVariable String libraryItemId,
            Authentication authentication) {
        String userId = authentication.getName();
        LibraryItemDto item = libraryService.toggleFavorite(userId, libraryItemId);
        return ResponseEntity.ok(item);
    }
}
