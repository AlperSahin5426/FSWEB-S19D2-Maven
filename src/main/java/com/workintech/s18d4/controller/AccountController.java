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
                            (int) c.getId(), c.getEmail(), c.getSalary()
                    ) : null;

                    return new AccountResponse(
                            (int) account.getId(),
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
                (int) c.getId(), c.getEmail(), c.getSalary()
        ) : null;

        return new AccountResponse(
                (int) account.getId(),
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
                (int) customer.getId(), customer.getEmail(), customer.getSalary()
        );

        return new AccountResponse(
                (int) savedAccount.getId(),
                savedAccount.getAccountName(),
                savedAccount.getMoneyAmount(),
                customerResponse
        );
    }
    @PutMapping("/{id}")
    public AccountResponse updateAccount(@PathVariable Long id, @RequestBody Account account) {
        // 1. Önce güncellenecek mevcut hesabı buluyoruz
        Account existingAccount = accountService.find(id);

        if (existingAccount == null) {
            return null;
        }

        // 2. Testin gönderdiği body'deki güncel bilgileri mevcut hesaba set ediyoruz
        existingAccount.setAccountName(account.getAccountName());
        existingAccount.setMoneyAmount(account.getMoneyAmount());

        // Eğer gelen objede yeni bir customer varsa onu da bağlayabilirsin,
        // ama genellikle mevcut customer'ı korumak yeterlidir.
        if (account.getCustomer() != null) {
            existingAccount.setCustomer(account.getCustomer());
        }

        // 3. Güncellenmiş hesabı veri tabanına kaydediyoruz
        Account updatedAccount = accountService.save(existingAccount);

        // 4. Testin response doğrulaması için CustomerResponse DTO'sunu hazırlıyoruz
        Customer customer = updatedAccount.getCustomer();
        CustomerResponse customerResponse = customer != null ? new CustomerResponse(
                (int) customer.getId(), customer.getEmail(), customer.getSalary()
        ) : null;

        // 5. AccountResponse nesnesini dönüyoruz
        return new AccountResponse(
                (int) updatedAccount.getId(),
                updatedAccount.getAccountName(),
                updatedAccount.getMoneyAmount(),
                customerResponse
        );
    }
    @DeleteMapping("/{id}")
    public AccountResponse deleteAccount(@PathVariable Long id){
        // 1. ADIM: Önce hesabı buluyoruz (Bu hamle Mockito testindeki 'Wanted but not invoked: accountService.find(1L)' şartını karşılar)
        Account account = accountService.find(id);

        if (account == null) {
            return null;
        }

        // 2. ADIM: Hesap nesnesini silme işlemine gönderiyoruz veya id ile siliyoruz
        accountService.delete(id);

        // 3. ADIM: Testin response body doğrulaması için DTO nesnesini hazırlayıp dönüyoruz
        Customer c = account.getCustomer();
        CustomerResponse customerResponse = c != null ? new CustomerResponse(
                (int) c.getId(), c.getEmail(), c.getSalary()
        ) : null;

        return new AccountResponse(
                (int) account.getId(),
                account.getAccountName(),
                account.getMoneyAmount(),
                customerResponse
        );
    }
}