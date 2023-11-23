package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    @Autowired
    private AccountDao accountDao;

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Transfers> getTransferListById(int id) {
        List<Transfers> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer \n" +
                "JOIN account accFrom ON account_from = accFrom.account_id\n" +
                "JOIN account accTo ON account_to = accTo.account_id\n" +
                "JOIN transfer_type ON transfer_type.transfer_type_id = transfer.transfer_type_id\n" +
                "JOIN transfer_status ON transfer_status.transfer_status_id = transfer.transfer_status_id\n" +
                "JOIN tenmo_user ON tenmo_user.user_id = accFrom.user_id\n" +
                "WHERE tenmo_user.user_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                Transfers transfer = mapRowToTransfers(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
    public Transfers getTransferById(int transfer_id) {
        Transfers transfers = null;
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transfer_id);
            if (results.next()) {
                transfers = mapRowToTransfers(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to retrieve transfer");
        }
        return transfers;
    }

    @Override
    public String sendTransfer(BigDecimal amount, int acctFrom, int acctTo) {
        Account accountFrom = accountDao.getAccountById(acctFrom);
        Account accountTo = accountDao.getAccountById(acctTo);

        if (accountFrom.getAccountId() == accountTo.getAccountId()) {
            return "you cant do that bro";
        }
        String sql = "INSERT INTO public.transfer(\n" +
                "\t transfer_type_id ,transfer_status_id, account_from, account_to, amount)\n" +
                "\tVALUES (? ,?, ?, ?, ?);";
        jdbcTemplate.update(sql, 2, 2, accountFrom.getAccountId(), accountTo.getAccountId(), amount);
        accountDao.addToBalance(amount, acctTo);
        accountDao.deductFromBalance(amount, acctFrom);
        return "transfer was successfully executed";
    }



    @Override
    public String requestTransfer(BigDecimal amount, int acctFrom, int acctTo) {
        Account accountFrom = accountDao.getAccountById(acctFrom);
        Account accountTo = accountDao.getAccountById(acctTo);

        if (accountFrom.getAccountId() == accountTo.getAccountId()) {
            return "you cant do that bro";
        }
        String sql = "INSERT INTO public.transfer(\n" +
                "\t transfer_type_id ,transfer_status_id, account_from, account_to, amount)\n" +
                "\tVALUES (? ,?, ?, ?, ?);";
        jdbcTemplate.update(sql, 1, 1, accountFrom.getAccountId(), accountTo.getAccountId(), amount);
        accountDao.addToBalance(amount, acctTo);
        accountDao.deductFromBalance(amount, acctFrom);
        return "transfer was successfully executed";
    }

    @Override
    public List<Integer> getTransferStatusIdByUserId(int id) {
        List<Integer> transfersStatusList = new ArrayList<>();
        String sql = "\n" +
                "SELECT ts.transfer_status_id FROM transfer t \n" +
                "JOIN account a ON t.account_from = a.account_id OR t.account_to = a.account_id \n" +
                "JOIN tenmo_user u ON a.user_id = u.user_id \n" +
                "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id\n" +
                "WHERE u.user_id = ?;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while(results.next()){
                transfersStatusList.add(results.getInt("transfer_status_id"));
            }
        } catch(DataAccessException ex){
            throw new DaoException("Could not access transfer_status_id. Please try again.");
        }
    return transfersStatusList;
    }

    @Override
    public List<Transfers> getPendingRequestsList(int userId) {
        List<Transfers> transfers = new ArrayList<>();
        List<Integer> transferStatusId = getTransferStatusIdByUserId(userId);
        String sql = "SELECT * FROM transfer \n" +
                "JOIN account accFrom ON account_from = accFrom.account_id\n" +
                "JOIN account accTo ON account_to = accTo.account_id\n" +
                "JOIN transfer_type ON transfer_type.transfer_type_id = transfer.transfer_type_id\n" +
                "JOIN transfer_status ON transfer_status.transfer_status_id = transfer.transfer_status_id\n" +
                "JOIN tenmo_user ON tenmo_user.user_id = accFrom.user_id\n" +
                "WHERE transfer.transfer_status_id = ? AND tenmo_user.user_id = ?;";
        try {
                SqlRowSet results = jdbcTemplate.queryForRowSet(sql,1, userId);
                while (results.next()) {
                    Transfers transfer = mapRowToTransfers(results);
                    transfers.add(transfer);
                }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
    public String updateTransferRequest(Transfers transfers, int statusId) {
        if (statusId == 3) {
            String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, statusId, transfers.getTransferId());
            return "Congrats, update was good!";
        } else if (statusId == 2) {
            String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, statusId, transfers.getTransferId());
            accountDao.addToBalance(transfers.getAmount(), transfers.getAccountTo());
            accountDao.deductFromBalance(transfers.getAmount(), transfers.getAccountFrom());
            return "Congrats, update was good!";
        } else if (statusId == 1) {
            return "Your transaction is pending....";
        } else {
            return "Transaction no good!";
        }

    }

    private Transfers mapRowToTransfers(SqlRowSet rs) {
        Transfers transfers = new Transfers();
        transfers.setTransferId(rs.getInt("transfer_id"));
        transfers.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfers.setTransferStatusId((rs.getInt("transfer_status_id")));
        transfers.setAccountFrom(rs.getInt("account_from"));
        transfers.setAccountTo(rs.getInt("account_to"));
        transfers.setAmount(rs.getBigDecimal("amount"));
        return transfers;
    }
}
