package com.example.apparchilog.models.requests;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("firstname")
    private String firstname;

    @SerializedName("email")
    private String email;
    @SerializedName("passwd")
    private String passwd;

    public RegisterRequest( String lastname, String firstname, String email, String passwd) {
        this.email = email;
        this.passwd = passwd;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
