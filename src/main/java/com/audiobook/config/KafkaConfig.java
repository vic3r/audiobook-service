package com.audiobook.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    
    @Value("${spring.kafka.topic.library-events:library-events}")
    private String libraryEventsTopic;
    
    @Value("${spring.kafka.topic.purchase-events:purchase-events}")
    private String purchaseEventsTopic;
    
    @Bean
    public NewTopic libraryEventsTopic() {
        return TopicBuilder.name(libraryEventsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    @Bean
    public NewTopic purchaseEventsTopic() {
        return TopicBuilder.name(purchaseEventsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
