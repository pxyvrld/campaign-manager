package com.example.campaign_manager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "emerald_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmeraldAccount {

    @Id
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

}
