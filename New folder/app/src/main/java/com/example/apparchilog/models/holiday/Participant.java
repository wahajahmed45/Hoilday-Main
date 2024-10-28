package com.example.apparchilog.models.holiday;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Participant implements Serializable {
    @SerializedName("id")
    private Long id;
    @SerializedName("nom")
    private String nom;
    @SerializedName("prenom")
    private String prenom;
    @SerializedName("email")
    private String email;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

}
