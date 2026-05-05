package com.example.campaign_manager.config;

import com.example.campaign_manager.model.EmeraldAccount;
import com.example.campaign_manager.repository.EmeraldAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final EmeraldAccountRepository emeraldAccountRepository;

    @Override
    public void run(String... args) {
        if (emeraldAccountRepository.findById(1L).isEmpty()) {
            EmeraldAccount account = EmeraldAccount.builder()
                    .id(1L)
                    .balance(new BigDecimal("10000.00"))
                    .build();
            emeraldAccountRepository.save(account);
        }
    }
}
