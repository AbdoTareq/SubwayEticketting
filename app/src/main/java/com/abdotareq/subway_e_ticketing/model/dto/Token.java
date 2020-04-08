package com.abdotareq.subway_e_ticketing.model.dto;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("token")
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
