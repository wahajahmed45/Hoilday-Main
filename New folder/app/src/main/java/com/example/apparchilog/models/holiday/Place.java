package com.example.apparchilog.models.holiday;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Place implements Serializable {
    @SerializedName("id")
    private Long id;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("rueNumero")
    private String rueNumero;
    @SerializedName("rue")
    private String rue;
    @SerializedName("ville")
    private String ville;
    @SerializedName("codePostal")
    private String codePostal;
    @SerializedName("pays")
    private String pays;

    public Place(Long id, double latitude, double longitude, String rueNumero, String rue, String ville, String codePostal, String pays) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rueNumero = rueNumero;
        this.rue = rue;
        this.ville = ville;
        this.codePostal = codePostal;
        this.pays = pays;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRueNumero() {
        return rueNumero;
    }

    public void setRueNumero(String rueNumero) {
        this.rueNumero = rueNumero;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    @Override
    public String toString() {
        return "Lieu{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", rueNumero=" + rueNumero +
                ", rue='" + rue + '\'' +
                ", ville='" + ville + '\'' +
                ", codePostal=" + codePostal +
                ", pays='" + pays + '\'' +
                '}';
    }
}
