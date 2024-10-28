package com.example.apparchilog.models.requests;

import com.example.apparchilog.utils.OffsetDateTimeSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.time.OffsetDateTime;

public class ActivityRequest {
    @SerializedName("id")
    private Long id;

    @SerializedName("nom")
    private String nom;

    @SerializedName("description")
    private String description;

    @SerializedName("dateDebut")
    @JsonAdapter(OffsetDateTimeSerializer.class)
    private OffsetDateTime dateDebut;

    @SerializedName("dateFin")
    @JsonAdapter(OffsetDateTimeSerializer.class)
    private OffsetDateTime dateFin;

    @SerializedName("placeDTO")
    private PlaceRequest placeRequest;

    public ActivityRequest(OffsetDateTime startDate, OffsetDateTime endDate,
                           String description, PlaceRequest lieu, String nom) {

        this.dateDebut = startDate;
        this.dateFin = endDate;
        this.description = description;
        this.placeRequest = lieu;
        this.nom = nom;
    }

    public OffsetDateTime getStartDate() {
        return dateDebut;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.dateDebut = startDate;
    }

    public OffsetDateTime getEndDate() {
        return dateFin;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.dateFin = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
