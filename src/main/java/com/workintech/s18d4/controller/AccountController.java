package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.AccountResponse;
import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.entity.Account;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.AccountService;
import com.workintech.s18d4.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final CustomerService customerService;

    @Autowired
    public AccountController(AccountService accountService, CustomerService customerService){
        this.accountService = accountService;
        this.customerService = customerService;
    }

    @GetMapping
    public List<AccountResponse> getAllAccount(){
        return accountService.findAll().stream()
                .map(account -> {
                    Customer c = account.getCustomer();
                    CustomerResponse customerResponse = c != null ? new CustomerResponse(
                            c.getId(), c.getEmail(), c.getSalary()
                    ) : null;

                    return new AccountResponse(
                            account.getId(),
                            account.getAccountName(),
                            account.getMoneyAmount(),
                            customerResponse
                    );
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AccountResponse getAccountById(@PathVariable Long id){
        Account account = accountService.find(id);
        Customer c = account.getCustomer();
        CustomerResponse customerResponse = c != null ? new CustomerResponse(
                c.getId(), c.getEmail(), c.getSalary()
        ) : null;

        return new AccountResponse(
                account.getId(),
                account.getAccountName(),
                account.getMoneyAmount(),
                customerResponse
        );
    }

    @PostMapping("/{customerId}")
    public AccountResponse newAccount(@PathVariable Long customerId, @RequestBody Account account){
        Customer customer = customerService.find(customerId);
        account.setCustomer(customer);

        Account savedAccount = accountService.save(account);

        CustomerResponse customerResponse = new CustomerResponse(
                customer.getId(), customer.getEmail(), customer.getSalary()
        );

        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getAccountName(),
                savedAccount.getMoneyAmount(),
                customerResponse
        );
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id){
        accountService.delete(id);
    }
}