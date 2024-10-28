package com.example.apparchilog.models.holiday;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Schedule implements Serializable {
    @SerializedName("id")
    private Long id;
    @SerializedName("title")
    private String title;
   /* @SerializedName("user")
    private User user;*/
}
