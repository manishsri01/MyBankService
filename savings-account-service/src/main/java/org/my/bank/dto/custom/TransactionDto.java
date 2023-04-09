package org.my.bank.dto.custom;

import org.jetbrains.annotations.NotNull;

public class TransactionDto {
    @NotNull
    private String accountNumber;
    @NotNull
    private Long amount;
    @NotNull
    private String narration;
    @NotNull
    private TransactionType transactionType;

    public TransactionDto() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}

