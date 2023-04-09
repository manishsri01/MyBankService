package org.my.bank.repository;

import org.my.bank.dto.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, String> {
    Optional<Savings> findByAccountNumber(String accountNumber);
}