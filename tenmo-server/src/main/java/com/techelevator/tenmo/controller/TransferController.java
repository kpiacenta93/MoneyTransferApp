package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransferController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TransferDao transferDao;

    public TransferController(UserDao userDao, AccountDao accountDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "account/transfer/{id}", method = RequestMethod.GET)
    public List<Transfers> getTransfersListById(@PathVariable int id){
        List<Transfers> transfersList = transferDao.getTransferListById(id);
        return transfersList;
    }


    @RequestMapping(path = "transfer/{id}")
    public Transfers getTransfersById(@PathVariable int id) {
       Transfers transfer = transferDao.getTransferById(id);
       if(transfer == null){
           System.out.println("SOMETHING HAPPENED");
       }
        return transferDao.getTransferById(id);
    }

    @RequestMapping(path = "transfer/{id}", method = RequestMethod.GET)
    public Transfers getSelectedTransferById(@PathVariable int id){
        Transfers transfers = transferDao.getTransferById(id);
        return transfers;
    }

    @RequestMapping(path = "transfer/send", method = RequestMethod.POST)
    public int sendTransfer(@RequestBody Transfers transfer){
        String output = transferDao.sendTransfer(transfer.getAmount(), transfer.getAccountFrom(), transfer.getAccountTo());
        return Integer.parseInt(output);
    }

    @RequestMapping(path = "transfer/request", method = RequestMethod.POST)
    public int requestTransfer(@RequestBody Transfers transfer){
        String output = transferDao.requestTransfer(transfer.getAmount(), transfer.getAccountFrom(), transfer.getAccountTo());
        return Integer.parseInt(output);
    }


    @RequestMapping(path = "account/pending/{id}")
    public List<Transfers> pendingTransactionList(@PathVariable int id){
        List<Transfers> results = transferDao.getPendingRequestsList(id);
        return results;


    }
    @RequestMapping(path = "transfer/status/{statusId}", method = RequestMethod.PUT)
    public String updateTransferRequest(@RequestBody Transfers transfers,@PathVariable  int statusId) {
        String results = transferDao.updateTransferRequest(transfers, statusId);
        return results;
    }

}
