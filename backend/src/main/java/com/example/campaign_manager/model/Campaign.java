package com.example.campaign_manager.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "campaigns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "campaign_keywords", joinColumns = @JoinColumn(name = "campaign_id"))
    @Column(name = "keyword")
    private List<String> keywords;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal bidAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal campaignFund;

    @Column(nullable = false)
    private Boolean status;

    @Column
    private String town;

    @Column(nullable = false)
    private Integer radius;
}
