package com.example.apparchilog.models.requests;

import com.google.gson.annotations.SerializedName;

public class ParticipantRequest {

    @SerializedName("email")
    private String email;
    @SerializedName("nom")
    private String nom;
    @SerializedName("prenom")
    private String prenom;

    public ParticipantRequest(String email, String nom, String prenom) {
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
    }
    public ParticipantRequest() {}
}