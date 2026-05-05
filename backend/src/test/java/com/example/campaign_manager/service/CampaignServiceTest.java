package com.example.campaign_manager.service;

import com.example.campaign_manager.dto.CampaignRequest;
import com.example.campaign_manager.dto.CampaignResponse;
import com.example.campaign_manager.model.Campaign;
import com.example.campaign_manager.exception.BusinessException;
import com.example.campaign_manager.model.EmeraldAccount;
import com.example.campaign_manager.repository.CampaignRepository;
import com.example.campaign_manager.repository.EmeraldAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignServiceTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private EmeraldAccountRepository emeraldAccountRepository;

    @InjectMocks
    private CampaignService campaignService;

    private EmeraldAccount emeraldAccount;
    private Campaign campaign;
    private CampaignRequest request;

    @BeforeEach
    void setUp() {
        emeraldAccount = EmeraldAccount.builder()
                .id(1L)
                .balance(new BigDecimal("10000.00"))
                .build();

        campaign = Campaign.builder()
                .id(1L)
                .name("Test Campaign")
                .keywords(List.of("shoes", "sneakers"))
                .bidAmount(new BigDecimal("1.50"))
                .campaignFund(new BigDecimal("500.00"))
                .status(true)
                .town("Warszawa")
                .radius(10)
                .build();

        request = CampaignRequest.builder()
                .name("Test Campaign")
                .keywords(List.of("shoes", "sneakers"))
                .bidAmount(new BigDecimal("1.50"))
                .campaignFund(new BigDecimal("500.00"))
                .status(true)
                .town("Warszawa")
                .radius(10)
                .build();
    }

    @Test
    void createCampaign_shouldDeductFundFromEmeraldBalance() {
        when(emeraldAccountRepository.findById(1L)).thenReturn(Optional.of(emeraldAccount));
        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);

        CampaignResponse response = campaignService.createCampaign(request);

        assertThat(emeraldAccount.getBalance()).isEqualByComparingTo("9500.00");
        assertThat(response.getName()).isEqualTo("Test Campaign");
        verify(emeraldAccountRepository).save(emeraldAccount);
        verify(campaignRepository).save(any(Campaign.class));
    }

    @Test
    void createCampaign_shouldThrowException_whenInsufficientBalance() {
        emeraldAccount.setBalance(new BigDecimal("100.00"));
        when(emeraldAccountRepository.findById(1L)).thenReturn(Optional.of(emeraldAccount));

        assertThatThrownBy(() -> campaignService.createCampaign(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Insufficient Emerald account balance");

        verify(campaignRepository, never()).save(any());
    }

    @Test
    void deleteCampaign_shouldRefundFundToEmeraldBalance() {
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
        when(emeraldAccountRepository.findById(1L)).thenReturn(Optional.of(emeraldAccount));

        campaignService.deleteCampaign(1L);

        assertThat(emeraldAccount.getBalance()).isEqualByComparingTo("10500.00");
        verify(campaignRepository).deleteById(1L);
        verify(emeraldAccountRepository).save(emeraldAccount);
    }

    @Test
    void updateCampaign_shouldDeductDifference_whenFundIncreased() {
        request.setCampaignFund(new BigDecimal("700.00"));
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
        when(emeraldAccountRepository.findById(1L)).thenReturn(Optional.of(emeraldAccount));
        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);

        campaignService.updateCampaign(1L, request);

        // fund wzrósł o 200 (700 - 500) więc saldo spada o 200
        assertThat(emeraldAccount.getBalance()).isEqualByComparingTo("9800.00");
    }

    @Test
    void updateCampaign_shouldRefundDifference_whenFundDecreased() {
        request.setCampaignFund(new BigDecimal("300.00"));
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
        when(emeraldAccountRepository.findById(1L)).thenReturn(Optional.of(emeraldAccount));
        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);

        campaignService.updateCampaign(1L, request);

        // fund spadł o 200 (500 - 300) więc saldo rośnie o 200
        assertThat(emeraldAccount.getBalance()).isEqualByComparingTo("10200.00");
    }

    @Test
    void getCampaignById_shouldThrowException_whenNotFound() {
        when(campaignRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> campaignService.getCampaignById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Campaign not found with id: 99");
    }
}
