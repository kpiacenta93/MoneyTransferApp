package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Account {

    public Account() {
    }

    public Account(int accountId, int userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    @JsonProperty("account_id")
    private int accountId;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("balance")
    private BigDecimal balance;

    public int getAccountId() {
        return accountId;
    }

    public int getAccountById(int accountId){
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }


    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
