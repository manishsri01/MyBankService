package org.my.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.my.bank.dto.Savings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SavingsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private static final String wrongAccountNumber = "12823238778";
    private static String accountNumber = "";

    @Test
    void openNewSavingAccountWithoutMandatoryHeader_Failed() throws Exception {
        mockMvc.perform(post("/savings/openNewAccount")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Customer not authorized")));
    }

    @Test
    void openNewSavingAccount_Success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/savings/openNewAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Account Created"))).andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Savings savings = objectMapper.readValue(result, Savings.class);
        accountNumber = savings.getAccountNumber();
    }

    @Test
    void depositMoneyWithWrongAccountNumber_Failed() throws Exception {
        mockMvc.perform(post("/savings/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1")
                        .content(wrongAccountNumber))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Account Does not exist")));
    }

    @Test
    void depositMoneyWithWrongCustomerId_Failed() throws Exception {
        mockMvc.perform(post("/savings/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "11111111")
                        .content(accountNumber))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Customer not authorized")));
    }

    @Test
    void depositMoney_Success() throws Exception {
        mockMvc.perform(post("/savings/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1")
                        .content(accountNumber))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Successful")));
    }

    @Test
    void withdrawMoneyWithWrongAccountNumber_Failed() throws Exception {
        mockMvc.perform(post("/savings/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1")
                        .content(wrongAccountNumber))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Account Does not exist")));
    }

    @Test
    void withdrawMoneyWithWrongCustomerId_Failed() throws Exception {
        mockMvc.perform(post("/savings/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "11111111")
                        .content(accountNumber))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Customer not authorized")));
    }

    @Test
    void withdrawMoney_Success() throws Exception {
        mockMvc.perform(post("/savings/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1")
                        .content(accountNumber))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Success")));
    }

    @Test
    void getTransactionListWithWrongAccount_Failed() throws Exception {
        mockMvc.perform(post("/savings/transactionList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1")
                        .content(wrongAccountNumber))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Account Does not exist")));
    }

    @Test
    void getTransactionList_Success() throws Exception {
        mockMvc.perform(post("/savings/transactionList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1")
                        .content(accountNumber))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Success")));
    }

    @Test
    void getBalance_Success() throws Exception {
        mockMvc.perform(post("/savings/getBalance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-forwarded-customer-id", "1")
                        .content(accountNumber))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Success")));
    }

}