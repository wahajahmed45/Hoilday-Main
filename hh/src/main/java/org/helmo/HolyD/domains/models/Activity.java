package org.helmo.HolyD.domains.models;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class Activity {

    private Long id;
    private String nom;
    private String description;
    private OffsetDateTime dateDebut;
    private OffsetDateTime dateFin;
    private Long owner;
    private Place place;
    private String status;
    private Vacation vacation;
    private List<Participant> participants = new ArrayList<>();

    public boolean intervalIsCrossed(OffsetDateTime newActivityStarDate, OffsetDateTime newActivityEndDate) {
        return ((this.dateDebut.isBefore(newActivityEndDate) || this.dateDebut.isEqual(newActivityEndDate)) &&
                (this.dateFin.isAfter(newActivityStarDate) || this.dateFin.isEqual(newActivityStarDate)));
    }

    public boolean isActivityCrossed(OffsetDateTime newActivityStarDate, OffsetDateTime newActivityEndDate) {
        return ((newActivityStarDate.isBefore(this.dateDebut) || newActivityStarDate.isEqual(this.dateDebut) || newActivityStarDate.isAfter(this.dateDebut)) &&
                (newActivityEndDate.isBefore(this.dateFin) || newActivityEndDate.isEqual(this.dateFin) || newActivityEndDate.isAfter(this.dateFin)));
    }
    // Méthode dans la classe Activity pour vérifier le conflit
    public boolean isConflicting(OffsetDateTime newActivityStartDate, OffsetDateTime newActivityEndDate) {
        // Vérifie si les activités se chevauchent ou sont adjacentes
        return (newActivityStartDate.isBefore(this.dateFin) && newActivityEndDate.isAfter(this.dateDebut)) ||
                (newActivityStartDate.isEqual(this.dateFin.plusDays(1)) || newActivityEndDate.isEqual(this.dateDebut.minusDays(1)));
    }
    public boolean userIsInside(User user) {
        return owner == user.getId();
       /* if (owner == user)
            return Objects.equals(owner.getId(), user.getId());
        return false;*/
    }

    public boolean userIsAlreadyInActivity() {
        return OffsetDateTime.now().isAfter(this.dateDebut) && OffsetDateTime.now().isBefore(dateFin);
    }
    public void removeParticipant(Long idParticipant) {
        participants.removeIf(p -> p.getId().equals(idParticipant));
    }

    public void setActivity(Activity activity) {
        nom = activity.getNom();
        description = activity.getDescription();
        dateDebut = activity.getDateDebut();
        dateFin = activity.getDateFin();

    }

    public void removeAllParticipant() {
        participants.clear();
    }

    public void addParticipantFromActivity(Participant participant) {
        if (!participants.contains(participant)) {
            participants.add(participant);
        }
    }
    public void setActivityStatus() {
        OffsetDateTime now = OffsetDateTime.now();

        if (dateFin.isBefore(now)) {
            this.status = "Past";
        } else if ((dateDebut.isBefore(now) || dateDebut.isEqual(now)) && (dateFin.isAfter(now) || dateFin.isEqual(now))) {
            status = "Present";
        } else if (dateDebut.isAfter(now)) {
            status = "Future";
        } else {
            status = "Unknown";
        }
    }
}