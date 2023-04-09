package org.my.bank.controller;

import io.micrometer.common.util.StringUtils;
import org.my.bank.dto.Savings;
import org.my.bank.dto.SavingsDetails;
import org.my.bank.dto.custom.TransactionDto;
import org.my.bank.service.SavingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/savings")
public class SavingsController {
    Logger LOGGER = LoggerFactory.getLogger(SavingsController.class);
    private final SavingsService savingsService;

    public SavingsController(SavingsService savingsService) {
        this.savingsService = savingsService;
    }

    @PostMapping("/openNewAccount")
    public ResponseEntity<?> openNewAccount(@RequestHeader Map<String, String> headers) {
        Savings result;
        try {
            String customerId = verifyHeaderAndGetCustomerId(headers);
            if (StringUtils.isNotEmpty(customerId)) {
                LOGGER.info("openNewAccount called for customer: {}", customerId);
                result = savingsService.openNewAccount(customerId);
            } else {
                LOGGER.warn("Customer not authorized");
                return new ResponseEntity<>("Customer not authorized", HttpStatus.FORBIDDEN);
            }
            LOGGER.info("openNewAccount end!");
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Error", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/transaction")
    public ResponseEntity<?> depositOrWithdrawMoney(@RequestHeader Map<String, String> headers, @RequestBody TransactionDto transactionDto) {
        SavingsDetails result;
        try {
            String customerId = verifyHeaderAndGetCustomerId(headers);
            if (StringUtils.isNotEmpty(customerId)) {
                LOGGER.info("depositOrWithdrawMoney called for customer: {}", customerId);
                result = savingsService.depositOrWithdrawMoney(transactionDto, Long.valueOf(customerId));
            } else {
                LOGGER.warn("Customer not authorized");
                return new ResponseEntity<>("Customer not authorized", HttpStatus.FORBIDDEN);
            }
            LOGGER.info("depositOrWithdrawMoney end!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/transactionList")
    public ResponseEntity<?> findSavingDetailsByAccountNumber(@RequestHeader Map<String, String> headers,
                                                              @RequestParam() String accountNumber,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Page<SavingsDetails> result;
        try {
            String customerId = verifyHeaderAndGetCustomerId(headers);
            if (StringUtils.isNotEmpty(customerId)) {
                LOGGER.info("findSavingDetailsByAccountNumber called for customer: {}", customerId);
                result = savingsService.findSavingDetailsByAccountNumber(accountNumber, PageRequest.of(page, size), Long.valueOf(customerId));
            } else {
                LOGGER.warn("Customer not authorized");
                return new ResponseEntity<>("Customer not authorized", HttpStatus.FORBIDDEN);
            }
            LOGGER.info("findSavingDetailsByAccountNumber end!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getBalance")
    public ResponseEntity<?> getBalance(@RequestHeader Map<String, String> headers,
                                        @RequestParam() String accountNumber) {
        Long result;
        try {
            String customerId = verifyHeaderAndGetCustomerId(headers);
            if (StringUtils.isNotEmpty(customerId)) {
                LOGGER.info("getBalance called for customer: {}", customerId);
                result = savingsService.getBalance(accountNumber, Long.valueOf(customerId));
            } else {
                LOGGER.warn("Customer not authorized");
                return new ResponseEntity<>("Customer not authorized", HttpStatus.FORBIDDEN);
            }
            LOGGER.info("getBalance end!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    private String verifyHeaderAndGetCustomerId(Map<String, String> headers) {
        if (headers.containsKey("x-forwarded-customer-id") && StringUtils.isNotEmpty(headers.get("x-forwarded-customer-id"))) {
            return headers.get("x-forwarded-customer-id");
        }
        return null;
    }
}
