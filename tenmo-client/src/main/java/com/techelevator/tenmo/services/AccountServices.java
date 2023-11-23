package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.zip.DataFormatException;

public class AccountServices {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate rs = new RestTemplate();
    private AuthenticatedUser currentUser;
    private String authToken;



    public AccountServices(RestTemplate rs) {
        this.rs = rs;
    }

    public AccountServices() {

    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getUserIdById(int id) {

        ResponseEntity<Account> response = rs.exchange(API_BASE_URL + "account/ " + id, HttpMethod.GET, makeAuthEntity(), Account.class);
        Account account = response.getBody();

        if (account != null) {
            return account.getAccountId();
        } else {
            throw new RuntimeException("Something happened");
        }
    }

    public int getCurrentUserAccountId(int id) {
        ResponseEntity<Account> response = rs.exchange(
                API_BASE_URL + "account/" + id, HttpMethod.GET, makeAuthEntity(), Account.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().getAccountId(); // Assuming the Account class has an getId() method
        } else {
            // Handle error or throw an exception
            return response.getBody().getAccountId();
        }
    }

    public BigDecimal getUserBalance(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<BigDecimal> response = rs.exchange(API_BASE_URL + "balance", HttpMethod.GET, entity, BigDecimal.class);
        return response.getBody();
    }
    public User[] usersList() {
        User[] allUsers = new User[0];
        ResponseEntity<User[]> response = rs.exchange(API_BASE_URL + "tenmo_users", HttpMethod.GET, makeAuthEntity(), User[].class);
        allUsers = response.getBody();
        return allUsers;
    }

    public boolean sendTransfer(String token, BigDecimal amount, int acctFrom, int acctTo) {
        Transfers transfer = new Transfers();
        transfer.setAccountFrom(acctFrom);
        transfer.setAccountTo(acctTo);
        transfer.setAmountSent(amount);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Transfers> entity = new HttpEntity<>(transfer, headers);

        ResponseEntity<TransferStatus> response = rs.postForEntity(
                API_BASE_URL + "transfer/send", entity, TransferStatus.class
        );

        return response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().isSuccess();
    }


    public int getAccountByUserId(int userId){

        ResponseEntity<Account> response = rs.exchange(API_BASE_URL + "account/" + userId, HttpMethod.GET, makeAuthEntity(), Account.class);
        Account account = response.getBody();

        if(account != null){
            return account.getAccountId();
        } else {
            throw new RuntimeException("Something happened");
        }

    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
