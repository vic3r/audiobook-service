package com.audiobook.service;

import com.audiobook.dto.LibraryItemDto;
import com.audiobook.dto.event.LibraryEvent;
import com.audiobook.dto.event.LibraryEventType;
import com.audiobook.model.LibraryItem;
import com.audiobook.model.User;
import com.audiobook.repository.AudiobookRepository;
import com.audiobook.repository.LibraryItemRepository;
import com.audiobook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LibraryService {
    
    @Autowired
    private LibraryItemRepository libraryItemRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AudiobookRepository audiobookRepository;
    
    @Autowired
    private KafkaEventService kafkaEventService;
    
    public List<LibraryItemDto> getUserLibrary(String userId) {
        List<LibraryItem> items = libraryItemRepository.findByUserIdOrderByLastPlayedAtDesc(userId);
        return items.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    public List<LibraryItemDto> getFavorites(String userId) {
        List<LibraryItem> items = libraryItemRepository.findByUserIdAndIsFavoriteTrue(userId);
        return items.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    @CacheEvict(value = "library", key = "#userId")
    public LibraryItemDto addToLibrary(String userId, String audiobookId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        audiobookRepository.findById(audiobookId)
            .orElseThrow(() -> new RuntimeException("Audiobook not found"));
        
        LibraryItem existingItem = libraryItemRepository
            .findByUserIdAndAudiobookId(userId, audiobookId)
            .orElse(null);
        
        if (existingItem != null) {
            return convertToDto(existingItem);
        }
        
        LibraryItem item = new LibraryItem();
        item.setUser(user);
        item.setAudiobook(audiobookRepository.findById(audiobookId).get());
        item.setAddedAt(LocalDateTime.now());
        
        item = libraryItemRepository.save(item);
        
        // Publish Kafka event
        LibraryEvent event = new LibraryEvent();
        event.setEventType(LibraryEventType.ADDED);
        event.setUserId(userId);
        event.setAudiobookId(audiobookId);
        event.setLibraryItemId(item.getId());
        kafkaEventService.publishLibraryEvent(event);
        
        return convertToDto(item);
    }
    
    @CacheEvict(value = "library", key = "#userId")
    public LibraryItemDto updatePlaybackPosition(String userId, String libraryItemId, Integer positionSeconds) {
        LibraryItem item = libraryItemRepository.findById(libraryItemId)
            .orElseThrow(() -> new RuntimeException("Library item not found"));
        
        if (!item.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        
        item.setLastPositionSeconds(positionSeconds);
        item.setLastPlayedAt(LocalDateTime.now());
        
        if (positionSeconds >= item.getAudiobook().getDurationMinutes() * 60 * 0.9) {
            item.setIsCompleted(true);
        }
        
        item = libraryItemRepository.save(item);
        
        // Publish Kafka event
        LibraryEvent event = new LibraryEvent();
        event.setEventType(LibraryEventType.POSITION_UPDATED);
        event.setUserId(userId);
        event.setAudiobookId(item.getAudiobook().getId());
        event.setLibraryItemId(libraryItemId);
        event.setPositionSeconds(positionSeconds);
        kafkaEventService.publishLibraryEvent(event);
        
        return convertToDto(item);
    }
    
    @CacheEvict(value = "library", key = "#userId")
    public LibraryItemDto toggleFavorite(String userId, String libraryItemId) {
        LibraryItem item = libraryItemRepository.findById(libraryItemId)
            .orElseThrow(() -> new RuntimeException("Library item not found"));
        
        if (!item.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        
        item.setIsFavorite(!item.getIsFavorite());
        item = libraryItemRepository.save(item);
        
        // Publish Kafka event
        LibraryEvent event = new LibraryEvent();
        event.setEventType(LibraryEventType.FAVORITE_TOGGLED);
        event.setUserId(userId);
        event.setAudiobookId(item.getAudiobook().getId());
        event.setLibraryItemId(libraryItemId);
        event.setIsFavorite(item.getIsFavorite());
        kafkaEventService.publishLibraryEvent(event);
        
        return convertToDto(item);
    }
    
    private LibraryItemDto convertToDto(LibraryItem item) {
        return new LibraryItemDto(
            item.getId(),
            new com.audiobook.dto.AudiobookDto(
                item.getAudiobook().getId(),
                item.getAudiobook().getTitle(),
                item.getAudiobook().getAuthor(),
                item.getAudiobook().getDescription(),
                item.getAudiobook().getNarrator(),
                item.getAudiobook().getDurationMinutes(),
                item.getAudiobook().getPrice(),
                item.getAudiobook().getCategory(),
                item.getAudiobook().getCoverImageUrl(),
                item.getAudiobook().getAudioFileUrl(),
                item.getAudiobook().getRating(),
                item.getAudiobook().getReviewCount(),
                item.getAudiobook().getPublishedDate()
            ),
            item.getLastPositionSeconds(),
            item.getIsCompleted(),
            item.getIsFavorite(),
            item.getAddedAt(),
            item.getLastPlayedAt()
        );
    }
}
