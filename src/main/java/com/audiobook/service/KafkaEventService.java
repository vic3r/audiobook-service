package com.audiobook.service;

import com.audiobook.dto.event.LibraryEvent;
import com.audiobook.dto.event.PurchaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaEventService {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaEventService.class);
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    @Value("${spring.kafka.topic.library-events:library-events}")
    private String libraryEventsTopic;
    
    @Value("${spring.kafka.topic.purchase-events:purchase-events}")
    private String purchaseEventsTopic;
    
    public void publishLibraryEvent(LibraryEvent event) {
        event.setTimestamp(LocalDateTime.now());
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(libraryEventsTopic, event.getUserId(), event);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Published library event: {} for user: {}", event.getEventType(), event.getUserId());
                } else {
                    logger.error("Failed to publish library event: {}", ex.getMessage());
                }
            });
        } catch (Exception e) {
            logger.error("Error publishing library event", e);
        }
    }
    
    public void publishPurchaseEvent(PurchaseEvent event) {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(purchaseEventsTopic, event.getUserId(), event);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Published purchase event for user: {} audiobook: {}", event.getUserId(), event.getAudiobookId());
                } else {
                    logger.error("Failed to publish purchase event: {}", ex.getMessage());
                }
            });
        } catch (Exception e) {
            logger.error("Error publishing purchase event", e);
        }
    }
}
