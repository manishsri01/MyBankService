package org.my.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.my.bank.dto.Customer;
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
        Customer customer = new Customer();
        customer.setCustomerName("");
        customer.setPassword("test-password");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customer);

        mockMvc.perform(post("/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Customer Name or Password is Empty")));
    }

    @Test
    void signUpCustomerWithEmptyPassword_Failed() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerName("test-user");
        customer.setPassword("");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customer);

        mockMvc.perform(post("/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Customer Name or Password is Empty")));
    }

    @Test
    void signUpCustomerWithCorrectValues_Success() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerName("test-user");
        customer.setPassword("test-password");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customer);

        mockMvc.perform(post("/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("customer created")));
    }

    @Test
    void signUpCustomerWithExistingCustomerName_Failed() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerName("test-user");
        customer.setPassword("test-password");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customer);

        mockMvc.perform(post("/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("Customer already exist")));
    }

    @Test
    void loginCustomerWithOutCustomerName_Failed() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerName("");
        customer.setPassword("test-password");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customer);

        mockMvc.perform(post("/customer/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Customer Name or Password is Empty")));
    }

    @Test
    void loginCustomerWithOutPassword_Failed() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerName("test-user");
        customer.setPassword("");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customer);

        mockMvc.perform(post("/customer/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Customer Name or Password is Empty")));
    }

    @Test
    void loginCustomerWithWrongCredential_Failed() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerName("test-user-wrong");
        customer.setPassword("test-password-wrong");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customer);

        mockMvc.perform(post("/customer/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Customer does not exist")));
    }

    @Test
    void loginCustomer_Success() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerName("test-user");
        customer.setPassword("test-password");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customer);

        mockMvc.perform(post("/customer/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login successfully")));
    }
}