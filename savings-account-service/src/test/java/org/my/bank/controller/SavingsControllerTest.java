package org.my.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.my.bank.dto.Savings;
import org.my.bank.dto.custom.TransactionDto;
import org.my.bank.dto.custom.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SavingsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private static final String wrongAccountNumber = "12823238778";
    private static String accountNumber = "";

    @Test
    @Order(1)
    void openNewSavingAccountWithoutMandatoryHeader_Failed() throws Exception {
        mockMvc.perform(post("/savings/openNewAccount")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Customer not authorized")));
    }

    @Test
    @Order(2)
    void openNewSavingAccount_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/savings/openNewAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("customerId")))
                .andExpect(content().string(containsString("accountNumber"))).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Savings savings = objectMapper.readValue(result, Savings.class);
        accountNumber = savings.getAccountNumber();
    }

    @Test
    @Order(3)
    void depositMoneyWithWrongAccountNumber_Failed() throws Exception {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAccountNumber(wrongAccountNumber);
        transactionDto.setAmount(500L);
        transactionDto.setTransactionType(TransactionType.DEPOSIT);
        transactionDto.setNarration("Test Deposit");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(transactionDto);

        mockMvc.perform(post("/savings/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1")
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Account Does not exist")));
    }

    @Test
    @Order(4)
    void depositMoneyWithWrongCustomerId_Failed() throws Exception {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAccountNumber(accountNumber);
        transactionDto.setAmount(500L);
        transactionDto.setTransactionType(TransactionType.DEPOSIT);
        transactionDto.setNarration("Test Deposit");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(transactionDto);

        mockMvc.perform(post("/savings/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "11111111")
                        .content(requestJson))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Customer not authorized")));
    }

    @Test
    @Order(5)
    void depositMoney_Success() throws Exception {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAccountNumber(accountNumber);
        transactionDto.setAmount(500L);
        transactionDto.setTransactionType(TransactionType.DEPOSIT);
        transactionDto.setNarration("Test Deposit");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(transactionDto);

        mockMvc.perform(post("/savings/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id")))
                .andExpect(content().string(containsString("savingsId")));
    }

    @Test
    @Order(6)
    void withdrawMoneyWithWrongAccountNumber_Failed() throws Exception {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAccountNumber(wrongAccountNumber);
        transactionDto.setAmount(500L);
        transactionDto.setTransactionType(TransactionType.WITHDRAW);
        transactionDto.setNarration("Test Withdraw");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(transactionDto);

        mockMvc.perform(post("/savings/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1")
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Account Does not exist")));
    }

    @Test
    @Order(7)
    void withdrawMoneyWithWrongCustomerId_Failed() throws Exception {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAccountNumber(accountNumber);
        transactionDto.setAmount(500L);
        transactionDto.setTransactionType(TransactionType.WITHDRAW);
        transactionDto.setNarration("Test Withdraw");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(transactionDto);

        mockMvc.perform(post("/savings/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "11111111")
                        .content(requestJson))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Customer not authorized")));
    }

    @Test
    @Order(8)
    void withdrawMoney_Success() throws Exception {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAccountNumber(accountNumber);
        transactionDto.setAmount(500L);
        transactionDto.setTransactionType(TransactionType.WITHDRAW);
        transactionDto.setNarration("Test Withdraw");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(transactionDto);

        mockMvc.perform(post("/savings/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id")))
                .andExpect(content().string(containsString("savingsId")));
    }

    @Test
    @Order(9)
    void getTransactionListWithWrongAccount_Failed() throws Exception {
        mockMvc.perform(get("/savings/transactionList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1")
                        .param("accountNumber", wrongAccountNumber)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Account Does not exist")));
    }

    @Test
    @Order(10)
    void getTransactionList_Success() throws Exception {
        mockMvc.perform(get("/savings/transactionList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1")
                        .param("accountNumber", accountNumber)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id")))
                .andExpect(content().string(containsString("savingsId")))
                .andExpect(content().string(containsString("amount")));
    }

    @Test
    @Order(11)
    void getBalance_Success() throws Exception {
        mockMvc.perform(get("/savings/getBalance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1")
                        .param("accountNumber", accountNumber))
                .andExpect(status().isOk());
    }

}