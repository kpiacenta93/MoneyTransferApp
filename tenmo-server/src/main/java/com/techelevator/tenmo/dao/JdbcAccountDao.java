package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{


    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        String sql = "SELECT * FROM account WHERE user_id = ?;";

        Account userAccount = null;

        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if(results.next()){
                userAccount = mapperToAccount(results);
            }
        } catch (DataAccessException e){
            System.out.println("could not retrieve account by user id.");
        }
        return userAccount;
    }

    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "SELECT balance FROM account WHERE user_id=?;";
        BigDecimal userBalance = null;
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if(results.next()){
                userBalance = results.getBigDecimal("balance");
            }
        } catch(DataAccessException ex){
            System.out.println("There was an error retrieving balance.");
        }
        return userBalance;
    }

    @Override
    public BigDecimal addToBalance(BigDecimal amount, int accountId) {
        Account account = getAccountById(accountId); //give us the details for account balance
        BigDecimal newBalance = account.getBalance().add(amount);
        String sql = "UPDATE public.account SET  balance=? WHERE account_id = ?;";
        try{
            jdbcTemplate.update(sql, newBalance, accountId);

        } catch (CannotGetJdbcConnectionException ex){
            throw new DaoException("cannot connect to the server or database.");
        }
        return account.getBalance();
    }

    @Override
    public BigDecimal deductFromBalance(BigDecimal amount, int accountId) {
        Account account = getAccountById(accountId); //give us the details for account balance
        BigDecimal newBalance = account.getBalance().subtract(amount);
        String sql = "UPDATE public.account SET  balance=? WHERE account_id = ?;";

        try{
            jdbcTemplate.update(sql, newBalance, accountId);

        } catch (CannotGetJdbcConnectionException ex){
            throw new DaoException("cannot connect to the server or database.");
        }
        return newBalance;
    }

    @Override
    public Account getAccountById(int accountFrom) {
        String sql = "SELECT * FROM account WHERE account_id = ?;";

        Account userAccount = null;

        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountFrom);
            if(results.next()){
                userAccount = mapperToAccount(results);
            }
        } catch (DataAccessException e){
            System.out.println("could not retrieve account by account id.");
        }
        return userAccount;
    }

    private Account mapperToAccount(SqlRowSet rowSet){
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_Id"));
        account.setBalance(rowSet.getBigDecimal("balance"));

        return account;
    }

}

