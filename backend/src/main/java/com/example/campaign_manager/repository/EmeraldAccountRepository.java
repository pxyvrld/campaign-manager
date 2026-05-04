package com.example.campaign_manager.repository;

import com.example.campaign_manager.model.EmeraldAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmeraldAccountRepository extends JpaRepository<EmeraldAccount, Long> {
}
