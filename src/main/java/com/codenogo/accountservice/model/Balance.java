package com.codenogo.accountservice.model;

import java.math.BigDecimal;

/**
 * @Author: Arnold Kizzitoh
 */
public class Balance {

    private String accountId;

    private BigDecimal balance;

    public Balance(String accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
