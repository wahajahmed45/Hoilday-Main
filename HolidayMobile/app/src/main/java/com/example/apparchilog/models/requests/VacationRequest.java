package com.example.apparchilog.models.requests;

import com.example.apparchilog.utils.OffsetDateTimeSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;
import java.time.OffsetDateTime;

public class VacationRequest implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("startDate")
    @JsonAdapter(OffsetDateTimeSerializer.class)
    private OffsetDateTime startDate;
    @SerializedName("endDate")
    @JsonAdapter(OffsetDateTimeSerializer.class)
    private OffsetDateTime endDate;

    @SerializedName("placeDTO")
    private PlaceRequest placeRequest;

    public VacationRequest(OffsetDateTime startDate, OffsetDateTime endDate, String description,
                           PlaceRequest placeRequest, String nom) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.placeRequest = placeRequest;
        this.name = nom;
    }

}
