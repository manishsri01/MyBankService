package org.my.bank.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringCloudConfigTest {
    @Autowired
    private WebTestClient webClient;
    private static String jwtToken = "";
    private static String customerName;

    @Test
    @Order(1)
    void gatewayRoutesCustomerServiceSignup() {
        int randomCustomer = ThreadLocalRandom.current().nextInt(1, 11111111 + 1);
        customerName = "test-customer" + randomCustomer;
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("customerName", customerName);
        hashMap.put("password", "password");

        webClient
                .post().uri("/customer/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(hashMap))
                .exchange()
                .expectStatus().isCreated()
                .expectBody();
    }

    @Test
    @Order(2)
    void gatewayRoutesCustomerServiceLogin() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("customerName", customerName);
        hashMap.put("password", "password");

        HashMap resultMap = webClient
                .post().uri("/customer/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(hashMap))
                .exchange()
                .expectStatus().isOk()
                .expectBody(HashMap.class)
                .returnResult().getResponseBody();

        if (resultMap != null && resultMap.containsKey("token")) {
            jwtToken = (String) resultMap.get("token");
        }

    }

    @Test
    @Order(3)
    void gatewayRoutesSavingsService() {
        webClient
                .post().uri("/savings/openNewAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-forwarded-customer-id", "1")
                .header("Authorization", "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isCreated()
                .expectBody();
    }
}