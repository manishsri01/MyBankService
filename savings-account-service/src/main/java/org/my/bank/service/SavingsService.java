package org.my.bank.service;

import org.my.bank.dto.Savings;
import org.my.bank.dto.SavingsDetails;
import org.my.bank.dto.custom.TransactionDto;
import org.my.bank.dto.custom.TransactionType;
import org.my.bank.exception.CustomException;
import org.my.bank.repository.SavingsDetailsRepository;
import org.my.bank.repository.SavingsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class SavingsService {
    private final SavingsRepository savingsRepository;
    private final SavingsDetailsRepository savingsDetailsRepository;

    public SavingsService(SavingsRepository savingsRepository, SavingsDetailsRepository savingsDetailsRepository) {
        this.savingsRepository = savingsRepository;
        this.savingsDetailsRepository = savingsDetailsRepository;
    }

    public Savings openNewAccount(String customerId) {
        //Create savings account object
        Savings newAccount = new Savings();
        newAccount.setCustomerId(Long.valueOf(customerId));
        //newAccount.setCurrentDate(new Date(System.currentTimeMillis()));
        newAccount.setAccountNumber(accountNumber());
        newAccount.setHomeBranch("Copenhagen Denmark");

        return savingsRepository.save(newAccount);
    }

    public SavingsDetails depositOrWithdrawMoney(TransactionDto transactionDto, Long customerId) throws CustomException {
        //Check amount
        if (transactionDto.getAmount() < 1) {
            throw new CustomException("Amount must be greater than zero", HttpStatus.BAD_REQUEST);
        }
        //Verify account
        Long savingsId = getSavingsIdAndVerifyCustomer(transactionDto.getAccountNumber(), customerId);

        SavingsDetails savingsDetails = new SavingsDetails();
        savingsDetails.setSavingsId(savingsId);
        savingsDetails.setNarration(transactionDto.getNarration());
        if (transactionDto.getTransactionType() == TransactionType.DEPOSIT) {
            savingsDetails.setAmount(transactionDto.getAmount());
        } else if (transactionDto.getTransactionType() == TransactionType.WITHDRAW) {
            //check if customer have enough balance
            Long balance = savingsDetailsRepository.getBalance(savingsId);
            if (balance == null || transactionDto.getAmount() > balance) {
                throw new CustomException("You do not have enough balance", HttpStatus.BAD_REQUEST);
            }
            savingsDetails.setAmount(-transactionDto.getAmount());
        } else {
            throw new CustomException("Transaction types not found", HttpStatus.BAD_REQUEST);
        }
        return savingsDetailsRepository.save(savingsDetails);
    }

    public Page<SavingsDetails> findSavingDetailsByAccountNumber(String accountNumber, Pageable pageable, Long customerId) throws CustomException {
        //Verify account
        Long savingsId = getSavingsIdAndVerifyCustomer(accountNumber, customerId);
        return savingsDetailsRepository.findBySavingsId(pageable, savingsId);
    }

    public Long getBalance(String accountNumber, Long customerId) throws CustomException {
        //Verify account
        Long savingsId = getSavingsIdAndVerifyCustomer(accountNumber, customerId);
        return savingsDetailsRepository.getBalance(savingsId);
    }

    private Long getSavingsIdAndVerifyCustomer(String accountNumber, Long customerId) throws CustomException {
        //Verify if user is owner of account
        Optional<Savings> savingsOptional = savingsRepository.findByAccountNumber(accountNumber);
        if (savingsOptional.isEmpty()) {
            throw new CustomException("Account Does not exist", HttpStatus.NOT_FOUND);
        }
        if (!Objects.equals(savingsOptional.get().getCustomerId(), customerId)) {
            throw new CustomException("Customer not authorized to do transaction", HttpStatus.FORBIDDEN);
        }
        return savingsOptional.get().getId();
    }

    private String accountNumber() {
        long min = 1000000000000L; //13 digits
        long max = 10000000000000L; //14 digits
        Random random = new Random();
        return String.valueOf(min + ((long) (random.nextDouble() * (max - min))));
    }
}
