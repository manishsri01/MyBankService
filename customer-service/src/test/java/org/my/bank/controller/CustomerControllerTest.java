package org.my.bank.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void signUpCustomerWithEmptyCustomerName_Failed() throws Exception {
        mockMvc.perform(post("/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("customerName", "")
                        .param("password", "test-password"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Customer Name is Empty")));
    }

    @Test
    void signUpCustomerWithEmptyPassword_Failed() throws Exception {
        mockMvc.perform(post("/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("customerName", "test-user")
                        .param("password", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Customer Password is Empty")));
    }

    @Test
    void signUpCustomerWithCorrectValues_Success() throws Exception {
        mockMvc.perform(post("/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("customerName", "test-user")
                        .queryParam("password", "test-password"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("customer created")));
    }

    @Test
    void signUpCustomerWithExistingCustomerName_Failed() throws Exception {
        mockMvc.perform(post("/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("customerName", "test-user")
                        .param("password", "test-password"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Customer already exist")));
    }

    @Test
    void loginCustomerWithOutCustomerName_Failed() throws Exception {
        mockMvc.perform(post("/customer/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("customerName", "")
                        .param("password", "test-password"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Customer Name is Empty")));
    }

    @Test
    void loginCustomerWithOutPassword_Failed() throws Exception {
        mockMvc.perform(post("/customer/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("customerName", "test-user")
                        .param("password", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Customer Password is Empty")));
    }

    @Test
    void loginCustomerWithWrongCredential_Failed() throws Exception {
        mockMvc.perform(post("/customer/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("customerName", "test-user-wrong")
                        .param("password", "test-password"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Customer does not exist")));
    }

    @Test
    void loginCustomer_Success() throws Exception {
        mockMvc.perform(post("/customer/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("customerName", "test-user")
                        .param("password", "test-password"))
                .andExpect(status().isOk());
    }
}