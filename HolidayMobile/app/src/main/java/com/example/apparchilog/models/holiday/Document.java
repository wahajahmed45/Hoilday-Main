package com.example.apparchilog.models.holiday;

import android.content.Intent;

import java.io.Serializable;

public class Document implements Serializable {

    private String fileName;
    private String fileUri;
    private String units;

    public Document(String fileName, String downloadUrl, String units) {
        this.fileName = fileName;
        this.fileUri = downloadUrl;
        this.units = units;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUri() {
        return fileUri;
    }

    public String getUnits() {
        return units;
    }
}
