package org.my.bank.repository;

import org.my.bank.dto.SavingsDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsDetailsRepository extends JpaRepository<SavingsDetails, String> {
    Page<SavingsDetails> findBySavingsId(Pageable page, Long savingsId);

    @Query(value = "select SUM(amount) from savings_details where savings_id = :savingsId", nativeQuery = true)
    Long getBalance(Long savingsId);
}