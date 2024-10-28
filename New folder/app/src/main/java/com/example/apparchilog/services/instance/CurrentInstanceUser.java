package com.example.apparchilog.services.instance;

import com.example.apparchilog.models.responses.LoginResponse;

public class CurrentInstanceUser {
    private static CurrentInstanceUser instance;
  //  private String token;
    private LoginResponse loginResponse;
    //private  String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbW1hQHRlc3QuY29tIiwiaWF0IjoxNzIxMDc2ODM4LCJleHAiOjE3MjEwOTEyMzh9.uZiZz4aVzWvoT3KdlwvDxjYxkU8N9fEo0jDMYZRHfg4";

    // Constructeur privé pour empêcher l'instantiation directe
    private CurrentInstanceUser() {
    }

    public static synchronized CurrentInstanceUser getInstance() {
        if (instance == null) {
            instance = new CurrentInstanceUser();
        }
        return instance;
    }

    /*public void setToken(String token) {
        this.token = token;
    }*/

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }

    public void setLoginResponse(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

    public String getToken() {
        if(loginResponse != null) {
            return loginResponse.getJwtToken();
        }
        return null;
    }
    public void setToken(String token) {
        if(loginResponse != null) {
            loginResponse.setJwToken(token);
        }
    }

}
