package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerResponse> getAllCustomer(){
        return customerService.findAll().stream()
                .map(customer -> new CustomerResponse(
                        customer.getId(),
                        customer.getEmail(),
                        customer.getSalary()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomerById(@PathVariable Long id){
        Customer customer = customerService.find(id);
        return new CustomerResponse(
                customer.getId(),
                customer.getEmail(),
                customer.getSalary()
        );
    }

    @PostMapping
    public CustomerResponse addCustomer(@RequestBody Customer customer){
        Customer savedCustomer = customerService.save(customer);
        return new CustomerResponse(
                savedCustomer.getId(),
                savedCustomer.getEmail(),
                savedCustomer.getSalary()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id){
        customerService.delete(id);
    }
}