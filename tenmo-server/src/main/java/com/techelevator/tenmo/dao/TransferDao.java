package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface TransferDao {

    public List<Transfers> getTransferListById(int userId);

    public Transfers getTransferById(int transfer_id);

    public String sendTransfer(BigDecimal amount, int userFrom, int userTo);

    public String requestTransfer(BigDecimal amount, int userFrom, int userTo);

    public List<Transfers> getPendingRequestsList(int id);

    public String updateTransferRequest(Transfers transfers, int userId);

    public List<Integer> getTransferStatusIdByUserId(int id);
    }
