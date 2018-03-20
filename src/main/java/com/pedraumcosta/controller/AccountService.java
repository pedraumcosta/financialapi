package com.pedraumcosta.controller;

import com.pedraumcosta.exceptions.BusinessException;
import com.pedraumcosta.model.Account;
import com.pedraumcosta.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(path = "/account/", method = RequestMethod.POST)
    public Account saveAccount(@RequestBody Account account) throws BusinessException {
        if (account == null || account.getName() == null) {
            throw new IllegalArgumentException("Not enough information to create an account");
        }

        if (account.getBalance().compareTo(new BigDecimal(0)) < 0) {
            throw new BusinessException("Can't create an account with negative balance");
        }

        accountRepository.save(account);

        return account;
    }

    @RequestMapping(path = "/accounts/", method = RequestMethod.GET)
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @RequestMapping(path = "/account/{accountName}", method = RequestMethod.GET)
    public Account getAccountWithName(@PathVariable("accountName") String name) {
        return accountRepository.findOneByName(name);
    }
}