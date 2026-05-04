package com.example.campaign_manager.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignRequest {

    @NotBlank(message = "Campaign name is required")
    private String name;

    @NotEmpty(message = "At least one keyword is required")
    private List<String> keywords;

    @NotNull(message = "Bid amount is required")
    @DecimalMin(value = "0.01", message = "Bid amount must be greater than 0")
    private BigDecimal bidAmount;

    @NotNull(message = "Campaign fund is required")
    @DecimalMin(value = "0.01", message = "Campaign fund must be greater than 0")
    private BigDecimal campaignFund;

    @NotNull(message = "Status is required")
    private Boolean status;

    private String town;

    @NotNull(message = "Radius is required")
    @DecimalMin(value = "1", message = "Radius must be at least 1 km")
    private Integer radius;

}
