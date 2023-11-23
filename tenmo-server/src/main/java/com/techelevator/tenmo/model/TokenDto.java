package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenDto {

    private String token;

    @JsonProperty("token")
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
