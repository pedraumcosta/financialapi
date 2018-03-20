package com.pedraumcosta.model;

import java.math.BigDecimal;

public class TransferRequest {
    Long id;
    String fromAccountName;
    String toAccountName;
    BigDecimal amount;
    TransferStatus status;

    public TransferRequest() {}

    public TransferRequest(String fromAccountName, String toAccountName, BigDecimal amount) {
        this.fromAccountName = fromAccountName;
        this.toAccountName = toAccountName;
        this.amount = amount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromAccountName() {
        return fromAccountName;
    }

    public void setFromAccountName(String fromAccountName) {
        this.fromAccountName = fromAccountName;
    }

    public String getToAccountName() {
        return toAccountName;
    }

    public void setToAccountName(String toAccountName) {
        this.toAccountName = toAccountName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }
}
