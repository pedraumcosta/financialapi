package com.pedraumcosta.controller;

import com.pedraumcosta.model.Account;
import com.pedraumcosta.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(path = "/accounts/", method = RequestMethod.POST)
    public Account saveAccount(@RequestBody Account account) {
        accountRepository.save(account);
        return account;
    }

    @RequestMapping(path = "/accounts/", method = RequestMethod.GET)
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }
}