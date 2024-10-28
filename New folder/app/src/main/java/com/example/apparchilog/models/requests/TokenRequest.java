package com.example.apparchilog.models.requests;

import com.google.gson.annotations.SerializedName;

public class TokenRequest {
    @SerializedName("idToken")
    private String idToken;

    public TokenRequest(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
