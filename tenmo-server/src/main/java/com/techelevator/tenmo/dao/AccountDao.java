package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;


public interface AccountDao {

    // need to return an account object for someone passing in a User ID --> refer to account controller;
    //public Account getAccountByAccountId(int id);
    public Account getAccountByUserId(int userId);
    public BigDecimal getBalance(int userId);
    public BigDecimal addToBalance(BigDecimal amount, int userId);
    public BigDecimal deductFromBalance(BigDecimal amount, int userId);
    Account getAccountById(int accountFrom);
}
