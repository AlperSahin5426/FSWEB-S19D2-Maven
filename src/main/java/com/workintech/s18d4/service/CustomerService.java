package com.workintech.s18d4.service;

import com.workintech.s18d4.entity.Customer;
import java.util.List;

public interface CustomerService {
    List<Customer> findAll();
    Customer find(Long id); // Long yerine int
    Customer save(Customer customer);
    Customer delete(Long id); // Long yerine int
}