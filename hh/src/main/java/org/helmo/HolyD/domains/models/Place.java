package org.helmo.HolyD.domains.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class Place {

    private Long id;
    private double longitude;
    private double latitude;
    private String rue;
    private String rueNumero;
    private String codePostal;
    private String ville;
    private String pays;

    private List<Vacation> vacations = new ArrayList<>();
    private List<Activity> activities = new ArrayList<>();

    public void setPlace(Place place) {
        if (place.getVille() != null && !place.getVille().isEmpty()
                && place.getPays() != null && !place.getPays().isEmpty()
                && place.getLatitude() != 0 && place.getLongitude() != 0) {
            this.ville = place.getVille();
            this.pays = place.getPays();
            this.codePostal = place.codePostal == null ? "" : place.codePostal;
            this.rue = place.getRue()!= null ? place.getRue() : "";
            this.rueNumero = place.getRueNumero() != null ? place.getRueNumero() : "";
            this.latitude = place.getLatitude();
            this.longitude = place.getLongitude();
        }

    }
}
