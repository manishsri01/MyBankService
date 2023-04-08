package org.my.bank.service;

import org.my.bank.dto.Customer;
import org.my.bank.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public Optional<Customer> findCustomerByName(String customerName) {
        return customerRepository.findByCustomerName(customerName);
    }

}
