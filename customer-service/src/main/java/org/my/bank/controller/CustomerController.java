package org.my.bank.controller;

import io.micrometer.common.util.StringUtils;
import org.my.bank.config.JwtConfig;
import org.my.bank.dto.Customer;
import org.my.bank.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

    Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;

    public CustomerController(CustomerService customerService, JwtConfig jwtConfig, PasswordEncoder passwordEncoder) {
        this.customerService = customerService;
        this.jwtConfig = jwtConfig;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpCustomer(@RequestBody Customer customer) {
        LOGGER.info("signUpCustomer called for: {}", customer.getCustomerName());
        try {
            if (StringUtils.isEmpty(customer.getCustomerName()) || StringUtils.isEmpty(customer.getPassword())) {
                LOGGER.warn("Customer Name or Password is Empty");
                return new ResponseEntity<>("Customer Name or Password is Empty", HttpStatus.BAD_REQUEST);
            }

            Optional<Customer> optionalCustomer = customerService.findCustomerByName(customer.getCustomerName());
            if (optionalCustomer.isPresent()) {
                LOGGER.warn("Customer already exist");
                return new ResponseEntity<>("Customer already exist", HttpStatus.FORBIDDEN);
            }
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customerService.saveCustomer(customer);

            LOGGER.info("signUpCustomer end!");
            return new ResponseEntity<>("customer created", HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Error", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody Customer customer) {
        LOGGER.info("loginCustomer called for: {}", customer.getCustomerName());
        try {
            if (StringUtils.isEmpty(customer.getCustomerName()) || StringUtils.isEmpty(customer.getPassword())) {
                LOGGER.warn("Customer Name or Password is Empty");
                return new ResponseEntity<>("Customer Name or Password is Empty", HttpStatus.BAD_REQUEST);
            }
            Optional<Customer> optionalCustomer = customerService.findCustomerByName(customer.getCustomerName());
            if (optionalCustomer.isEmpty()) {
                LOGGER.warn("Customer does not exist");
                return new ResponseEntity<>("Customer does not exist", HttpStatus.NOT_FOUND);
            }
            if (passwordEncoder.matches(customer.getPassword(), optionalCustomer.get().getPassword())) {
                return new ResponseEntity<>(jwtConfig.generateJwtToken(optionalCustomer.get()), HttpStatus.OK);
            } else {
                LOGGER.warn("Customer name or password does not match");
                return new ResponseEntity<>("Customer name or password does not match", HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            LOGGER.error("Error", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

}
