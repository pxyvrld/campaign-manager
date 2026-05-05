package com.example.campaign_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class KeywordsAndTownsConfig {

    @Bean
    public List<String> predefinedTowns() {
        return List.of(
                "Warszawa",
                "Kraków",
                "Gdańsk",
                "Wrocław",
                "Poznań",
                "Łódź",
                "Katowice",
                "Lublin",
                "Białystok",
                "Rzeszów"
        );
    }

    @Bean
    public List<String> predefinedKeywords() {
        return List.of(
                "shoes",
                "sneakers",
                "boots",
                "clothing",
                "electronics",
                "furniture",
                "sports",
                "books",
                "toys",
                "garden"
        );
    }
}
