package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Locale;

@RestController
    public class AccountController {
    private UserDao userDao;
    private AccountDao accountDao;

    public AccountController(UserDao userDao, AccountDao accountDao){
        this.userDao = userDao;
        this.accountDao = accountDao;
    }
    @RequestMapping(path = "balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal currentUser){
         int userId = userDao.getUserByUsername(currentUser.getName()).getId();
         BigDecimal balance = accountDao.getBalance(userId);
         return balance;
    }

    @RequestMapping(path = "account", method = RequestMethod.GET)
    public int getCurrentUser(Principal currentUser){
        int account = accountDao.getAccountById(Integer.parseInt(currentUser.getName())).getUserId();

        return account;
    }
    @RequestMapping(path = "balance/{id}", method = RequestMethod.GET)
    public BigDecimal getBalanceByUserId(@PathVariable int id){
        BigDecimal balanceById = accountDao.getBalance(id);

        return balanceById;
    }

    @RequestMapping(path = "account/{id}", method = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable int id){
        Account account = accountDao.getAccountByUserId(id);
        return account;
    }

    @RequestMapping(path = "greeting", method = RequestMethod.GET)
    public String greeting(){
        return "hello my friends";
    }

}
