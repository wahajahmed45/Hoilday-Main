package com.example.apparchilog.models.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LoginResponse implements Serializable {
    @SerializedName("id")
    private long id;
    @SerializedName("jwtToken")
    private String jwtToken;
    @SerializedName("email")
    private String email;
    @SerializedName("pictureUrl")
    private String pictureUrl;

    @SerializedName("lastname")
    private String lastname;
    @SerializedName("firstname")
    private String firstname;


    @SerializedName("roles")
    private List<String> roles;
    public long getId() {return id;}
    public String getJwtToken() {
        return jwtToken;
    }

    public String getEmail() {
        return email;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setJwToken(String token) {
        this.jwtToken = token;
    }

}
