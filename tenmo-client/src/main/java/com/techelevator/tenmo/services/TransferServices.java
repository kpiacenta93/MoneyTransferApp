package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import org.springframework.web.client.RestTemplate;

import java.util.List;

public class TransferServices {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate rs = new RestTemplate();
    private AuthenticatedUser currentUser;
    private AccountServices accountServices;
    private String authToken;
    private User userService;


    public TransferServices() {
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    public TransferServices(AuthenticatedUser currentUser, RestTemplate rs) {
        this.currentUser = currentUser;
        this.rs = rs;
    }

    public Transfers[] printTransferListForCurrentUser(String token, int userId) {
        String url = API_BASE_URL + "account/transfer/" + userId;

        ResponseEntity<Transfers[]> response = rs.exchange(url, HttpMethod.GET, makeAuthEntity(), Transfers[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Transfers[] transferArray = response.getBody();

            if (transferArray == null || transferArray.length == 0) {
                System.out.println("No transfers found or empty array.");
            } else {
                for (Transfers transfer : transferArray) {
                    System.out.println("-------------------------------------------");
                    System.out.println("Transfers");
                    System.out.println("ID:          From/To:               Amount");
                    System.out.println("-------------------------------------------");

                    String fromTo = ("From: " + transfer.getAccountFrom() + " To: " + transfer.getAccountTo());
                    System.out.printf("%-12d%-22s$ %,.2f%n", transfer.getTransferId(), fromTo, transfer.getAmountSent());
                }
            }

            return transferArray;
        } else {
            throw new RuntimeException("Failed to retrieve transfers: " + response.getStatusCode());
        }
    }

    public TransferStatus[] getPendingTransactions(String token, int userId){
        String url = API_BASE_URL + "account/pending/" + userId;
        Transfers transfer = new Transfers();

        ResponseEntity<List<TransferStatus>> response = rs.exchange(
                url,
                HttpMethod.GET,
                makeAuthEntity(),
                new ParameterizedTypeReference<List<TransferStatus>>() {});

        if (response.getStatusCode() == HttpStatus.OK) {
            List<TransferStatus> pendingList = response.getBody();

            if (pendingList == null || pendingList.isEmpty()) {
                System.out.println("No pending transactions exist currently.");
            } else {
                for (TransferStatus status : pendingList) {
                    if(status.getTransferStatusId() == 1){
                        System.out.println("You have pending transfers from: " + transfer.getAccountFrom());
                    }

                }
            }

            TransferStatus[] pendingArray = new TransferStatus[pendingList.size()];
            Account accountFrom = new Account();

            return pendingList.toArray(pendingArray);
        } else {
            throw new RuntimeException("something went terribly wrong");
        }
    }




        private HttpEntity<Void> makeAuthEntity () {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity<>(headers);
            return entity;
        }


    }

