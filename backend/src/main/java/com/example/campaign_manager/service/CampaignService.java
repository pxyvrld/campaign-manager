package com.example.campaign_manager.service;

import com.example.campaign_manager.dto.CampaignRequest;
import com.example.campaign_manager.dto.CampaignResponse;
import com.example.campaign_manager.dto.EmeraldAccountResponse;
import com.example.campaign_manager.exception.BusinessException;
import com.example.campaign_manager.model.Campaign;
import com.example.campaign_manager.model.EmeraldAccount;
import com.example.campaign_manager.repository.CampaignRepository;
import com.example.campaign_manager.repository.EmeraldAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final EmeraldAccountRepository emeraldAccountRepository;

    public List<CampaignResponse> getAllCampaigns() {
        return campaignRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CampaignResponse getCampaignById(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Campaign not found with id: " + id));
        return toResponse(campaign);
    }

    @Transactional
    public CampaignResponse createCampaign(CampaignRequest request) {
        EmeraldAccount account = getEmeraldAccount();

        if (account.getBalance().compareTo(request.getCampaignFund()) < 0) {
            throw new BusinessException("Insufficient Emerald account balance");
        }

        account.setBalance(account.getBalance().subtract(request.getCampaignFund()));
        emeraldAccountRepository.save(account);

        Campaign campaign = Campaign.builder()
                .name(request.getName())
                .keywords(request.getKeywords())
                .bidAmount(request.getBidAmount())
                .campaignFund(request.getCampaignFund())
                .status(request.getStatus())
                .town(request.getTown())
                .radius(request.getRadius())
                .build();

        return toResponse(campaignRepository.save(campaign));
    }

    @Transactional
    public CampaignResponse updateCampaign(Long id, CampaignRequest request) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Campaign not found with id: " + id));

        EmeraldAccount account = getEmeraldAccount();

        BigDecimal diff = request.getCampaignFund().subtract(campaign.getCampaignFund());

        if (diff.compareTo(BigDecimal.ZERO) > 0) {
            if (account.getBalance().compareTo(diff) < 0) {
                throw new BusinessException("Insufficient Emerald account balance");
            }
            account.setBalance(account.getBalance().subtract(diff));
        } else {
            account.setBalance(account.getBalance().subtract(diff));
        }

        emeraldAccountRepository.save(account);

        campaign.setName(request.getName());
        campaign.setKeywords(request.getKeywords());
        campaign.setBidAmount(request.getBidAmount());
        campaign.setCampaignFund(request.getCampaignFund());
        campaign.setStatus(request.getStatus());
        campaign.setTown(request.getTown());
        campaign.setRadius(request.getRadius());

        return toResponse(campaignRepository.save(campaign));
    }

    @Transactional
    public void deleteCampaign(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Campaign not found with id: " + id));

        EmeraldAccount account = getEmeraldAccount();
        account.setBalance(account.getBalance().add(campaign.getCampaignFund()));
        emeraldAccountRepository.save(account);

        campaignRepository.deleteById(id);
    }

    public EmeraldAccountResponse getEmeraldBalance() {
        EmeraldAccount account = getEmeraldAccount();
        return EmeraldAccountResponse.builder()
                .balance(account.getBalance())
                .build();
    }

    private EmeraldAccount getEmeraldAccount() {
        return emeraldAccountRepository.findById(1L)
                .orElseThrow(() -> new BusinessException("Emerald account not found"));
    }

    private CampaignResponse toResponse(Campaign campaign) {
        return CampaignResponse.builder()
                .id(campaign.getId())
                .name(campaign.getName())
                .keywords(campaign.getKeywords())
                .bidAmount(campaign.getBidAmount())
                .campaignFund(campaign.getCampaignFund())
                .status(campaign.getStatus())
                .town(campaign.getTown())
                .radius(campaign.getRadius())
                .build();
    }
}
