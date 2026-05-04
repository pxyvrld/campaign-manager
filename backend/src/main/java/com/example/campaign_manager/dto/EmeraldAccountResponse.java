package com.example.campaign_manager.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmeraldAccountResponse {

    private BigDecimal balance;
}
