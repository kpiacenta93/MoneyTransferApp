package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferStatus {


    @JsonProperty("transfer_status_id")
    private int transferStatusId;


    @JsonProperty("transfer_status_desc")
    private String transferStatusDescription;

    public boolean isSuccess;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public TransferStatus() {
    }

    @JsonCreator
    public TransferStatus(@JsonProperty("transfer_status_id") int transferStatusId,
                          @JsonProperty("transfer_status_desc") String transferStatusDescription) {
        this.transferStatusId = transferStatusId;
        this.transferStatusDescription = transferStatusDescription;
    }


    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getTransferStatusDescription() {
        return transferStatusDescription;
    }

    public void setTransferStatusDescription(String transferStatusDescription) {
        this.transferStatusDescription = transferStatusDescription;
    }

    @Override
    public String toString() {
        return "Amount of pending request: " + transferStatusId;
    }

}
