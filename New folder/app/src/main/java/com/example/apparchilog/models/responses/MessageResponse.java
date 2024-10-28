package com.example.apparchilog.models.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageResponse implements Serializable {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}
