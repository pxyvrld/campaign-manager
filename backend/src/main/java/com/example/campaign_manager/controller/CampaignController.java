package com.example.campaign_manager.controller;

import com.example.campaign_manager.dto.CampaignRequest;
import com.example.campaign_manager.dto.CampaignResponse;
import com.example.campaign_manager.dto.EmeraldAccountResponse;
import com.example.campaign_manager.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CampaignController {

    private final CampaignService campaignService;

    private final List<String> predefinedTowns;
    private final List<String> predefinedKeywords;

    @GetMapping
    public ResponseEntity<List<CampaignResponse>> getAllCampaigns() {
        return ResponseEntity.ok(campaignService.getAllCampaigns());
    }

    @PostMapping
    public ResponseEntity<CampaignResponse> createCampaign(@Valid @RequestBody CampaignRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(campaignService.createCampaign(request));
    }

    @GetMapping("/account/emerald-balance")
    public ResponseEntity<EmeraldAccountResponse> getEmeraldBalance() {
        return ResponseEntity.ok(campaignService.getEmeraldBalance());
    }

    @GetMapping("/towns")
    public ResponseEntity<List<String>> getTowns() {
        return ResponseEntity.ok(predefinedTowns);
    }

    @GetMapping("/keywords")
    public ResponseEntity<List<String>> getKeywords() {
        return ResponseEntity.ok(predefinedKeywords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> getCampaignById(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.getCampaignById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampaignResponse> updateCampaign(@PathVariable Long id,
                                                           @Valid @RequestBody CampaignRequest request) {
        return ResponseEntity.ok(campaignService.updateCampaign(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }

}
