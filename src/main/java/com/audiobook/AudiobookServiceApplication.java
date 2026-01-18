package com.audiobook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class AudiobookServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AudiobookServiceApplication.class, args);
    }
}
