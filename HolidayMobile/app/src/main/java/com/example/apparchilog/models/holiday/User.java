package com.example.apparchilog.models.holiday;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    @SerializedName("id")
    private Long id;
    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastname")
    private String lastname;
    @SerializedName("email")
    private String email;

    @SerializedName("roles")
    private List<Role> roles = new ArrayList<>();
    @SerializedName("vacationDTO")
    private List<Vacation> vacationDTO;

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public List<Vacation> getVacationDTO() {
        return vacationDTO;
    }
}
