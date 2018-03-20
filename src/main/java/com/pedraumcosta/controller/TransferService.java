package com.pedraumcosta.controller;

import com.pedraumcosta.exceptions.BusinessException;
import com.pedraumcosta.model.Account;
import com.pedraumcosta.model.TransferRequest;
import com.pedraumcosta.model.TransferStatus;
import com.pedraumcosta.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@RestController
@Transactional
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(path = "/transfer/", method = RequestMethod.POST)
    public TransferRequest transferFunds(@RequestBody TransferRequest transferRequest)
            throws BusinessException {

        //validate inputs
        if (transferRequest == null || transferRequest.getFromAccountName() == null
                || transferRequest.getToAccountName() == null || transferRequest.getAmount() == null) {
            throw new IllegalArgumentException("Not enough information to process transfer request");
        }

        transferRequest.setStatus(TransferStatus.PROCESSING);

        Account fromAccount = accountRepository.findOneByName(transferRequest.getFromAccountName());
        Account toAccount = accountRepository.findOneByName(transferRequest.getToAccountName());

        validateTransferAccounts(fromAccount, toAccount);
        validateFromAccountBalance(fromAccount, transferRequest.getAmount());

        debitAccount(fromAccount, transferRequest.getAmount());
        creditAccount(toAccount, transferRequest.getAmount());

        transferRequest.setStatus(TransferStatus.COMPLETED);

        return transferRequest;
    }

    private void validateTransferAccounts(Account fromAccount, Account toAccount) throws BusinessException {
        if (fromAccount == null) throw new BusinessException("Source account do not exists");
        if (toAccount == null) throw new BusinessException("Destination account do not exists");
    }

    private void validateFromAccountBalance(Account fromAccount, BigDecimal amount) throws BusinessException{
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("Not enough funds on source account");
        }
    }

    private void debitAccount(Account fromAccount, BigDecimal amount) {
        BigDecimal newBalance = fromAccount.getBalance().subtract(amount);
        fromAccount.setBalance(newBalance);
        accountRepository.save(fromAccount);
    }

    private void creditAccount(Account toAccount, BigDecimal amount) {
        BigDecimal newBalance = toAccount.getBalance().add(amount);
        toAccount.setBalance(newBalance);
        accountRepository.save(toAccount);
    }
}