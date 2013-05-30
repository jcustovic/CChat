package hr.chus.cchat.db.repository;

import java.util.List;

import hr.chus.cchat.model.db.jpa.LanguageProvider;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Transactional(readOnly = true)
public interface LanguageProviderRepository extends JpaRepository<LanguageProvider, Integer> {
    
    @Query(value = "SELECT lp FROM LanguageProvider lp WHERE ?1 LIKE CONCAT(lp.prefix, '%') ORDER BY LENGTH(lp.prefix) DESC")
    List<LanguageProvider> findBestMatchByPrefix(String p_msisdn);
    
    @Query(value = "SELECT lp FROM LanguageProvider lp WHERE ?1 LIKE CONCAT(lp.prefix, '%') ORDER BY LENGTH(lp.prefix) DESC")
    List<LanguageProvider> findBestMatchByPrefix(String p_msisdn, Pageable p_pageable);

}
