package com.audiobook.service;

import com.audiobook.model.Audiobook;
import com.audiobook.repository.AudiobookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeederService implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataSeederService.class);
    
    @Autowired
    private AudiobookRepository audiobookRepository;
    
    @Override
    @Transactional
    public void run(String... args) {
        if (audiobookRepository.count() == 0) {
            logger.info("Seeding sample audiobooks...");
            seedAudiobooks();
            logger.info("Sample audiobooks seeded successfully!");
        } else {
            logger.info("Database already contains audiobooks. Skipping seed.");
        }
    }
    
    private void seedAudiobooks() {
        List<Audiobook> audiobooks = new ArrayList<>();
        
        // Fiction audiobooks
        audiobooks.add(createAudiobook(
            "The Great Gatsby",
            "F. Scott Fitzgerald",
            "A classic American novel about the Jazz Age, wealth, and the pursuit of the American Dream.",
            "Jake Gyllenhaal",
            180,
            new BigDecimal("19.99"),
            "Fiction",
            "https://images-na.ssl-images-amazon.com/images/I/81QuEGw8VPL.jpg",
            4.5,
            1248
        ));
        
        audiobooks.add(createAudiobook(
            "To Kill a Mockingbird",
            "Harper Lee",
            "A gripping tale of racial injustice and childhood innocence in the American South.",
            "Sissy Spacek",
            375,
            new BigDecimal("24.99"),
            "Fiction",
            "https://images-na.ssl-images-amazon.com/images/I/81gepf1eMqL.jpg",
            4.8,
            2156
        ));
        
        audiobooks.add(createAudiobook(
            "1984",
            "George Orwell",
            "A dystopian social science fiction novel about totalitarian surveillance.",
            "Simon Prebble",
            342,
            new BigDecimal("22.99"),
            "Fiction",
            "https://images-na.ssl-images-amazon.com/images/I/81zZEkP3gBL.jpg",
            4.6,
            1834
        ));
        
        // Self-Help
        audiobooks.add(createAudiobook(
            "Atomic Habits",
            "James Clear",
            "An easy and proven way to build good habits and break bad ones.",
            "James Clear",
            320,
            new BigDecimal("16.99"),
            "Self-Help",
            "https://images-na.ssl-images-amazon.com/images/I/81YkqyaFVEL.jpg",
            4.7,
            4523
        ));
        
        audiobooks.add(createAudiobook(
            "The 7 Habits of Highly Effective People",
            "Stephen R. Covey",
            "A powerful lesson in personal change and effectiveness.",
            "Stephen R. Covey",
            432,
            new BigDecimal("21.99"),
            "Self-Help",
            "https://images-na.ssl-images-amazon.com/images/I/71wM6xRdvFL.jpg",
            4.5,
            3892
        ));
        
        // Business
        audiobooks.add(createAudiobook(
            "The Lean Startup",
            "Eric Ries",
            "How today's entrepreneurs use continuous innovation to create radically successful businesses.",
            "Eric Ries",
            312,
            new BigDecimal("18.99"),
            "Business",
            "https://images-na.ssl-images-amazon.com/images/I/81-QB7nD4-DL.jpg",
            4.4,
            2856
        ));
        
        // Science
        audiobooks.add(createAudiobook(
            "Sapiens: A Brief History of Humankind",
            "Yuval Noah Harari",
            "A groundbreaking narrative of humanity's creation and evolution.",
            "Derek Perkins",
            464,
            new BigDecimal("23.99"),
            "Science",
            "https://images-na.ssl-images-amazon.com/images/I/81WmojBxvbL.jpg",
            4.6,
            3245
        ));
        
        // Mystery
        audiobooks.add(createAudiobook(
            "The Girl with the Dragon Tattoo",
            "Stieg Larsson",
            "A spellbinding amalgam of murder mystery, family saga, and love story.",
            "Simon Vance",
            465,
            new BigDecimal("20.99"),
            "Mystery",
            "https://images-na.ssl-images-amazon.com/images/I/81Kc8OsbDxL.jpg",
            4.3,
            1765
        ));
        
        // Biography
        audiobooks.add(createAudiobook(
            "Becoming",
            "Michelle Obama",
            "An intimate and inspiring memoir by the former First Lady of the United States.",
            "Michelle Obama",
            768,
            new BigDecimal("26.99"),
            "Biography",
            "https://images-na.ssl-images-amazon.com/images/I/81h2gWPTYJL.jpg",
            4.8,
            5621
        ));
        
        // Technology
        audiobooks.add(createAudiobook(
            "The Innovator's Dilemma",
            "Clayton M. Christensen",
            "When new technologies cause great firms to fail.",
            "Don Leslie",
            336,
            new BigDecimal("19.99"),
            "Technology",
            "https://images-na.ssl-images-amazon.com/images/I/71q7JE9RHhL.jpg",
            4.5,
            2134
        ));
        
        audiobookRepository.saveAll(audiobooks);
        logger.info("Created {} sample audiobooks", audiobooks.size());
    }
    
    private Audiobook createAudiobook(String title, String author, String description,
                                     String narrator, int durationMinutes, BigDecimal price,
                                     String category, String coverImageUrl, double rating, int reviewCount) {
        Audiobook audiobook = new Audiobook();
        audiobook.setTitle(title);
        audiobook.setAuthor(author);
        audiobook.setDescription(description);
        audiobook.setNarrator(narrator);
        audiobook.setDurationMinutes(durationMinutes);
        audiobook.setPrice(price);
        audiobook.setCategory(category);
        audiobook.setCoverImageUrl(coverImageUrl);
        audiobook.setRating(rating);
        audiobook.setReviewCount(reviewCount);
        audiobook.setPublishedDate(LocalDateTime.now().minusMonths((long)(Math.random() * 24)));
        audiobook.setAudioFileUrl("https://example.com/audio/" + title.toLowerCase().replaceAll(" ", "-") + ".mp3");
        return audiobook;
    }
}
