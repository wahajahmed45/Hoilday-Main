package org.helmo.HolyD.domains.models;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class Vacation {

    private Long id;
    private String name;
    private String description;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private User owner;
    private Place place;
    private String status;
    private List<Participant> participants = new ArrayList<>();
    private List<Activity> activities = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private List<Document> documents = new ArrayList<>();

    public void initList() {
        participants = new ArrayList<>();
        activities = new ArrayList<>();
        messages = new ArrayList<>();
        documents = new ArrayList<>();
    }

    public boolean intervalIsInside(OffsetDateTime activityDateDebut, OffsetDateTime activityDateFin) {
        return ((activityDateDebut.isAfter(this.startDate) || activityDateDebut.isEqual(this.startDate)) &&
                (activityDateFin.isBefore(this.endDate) || activityDateFin.isEqual(this.endDate)));
    }

    /*
     public boolean intervalIsInside(OffsetDateTime activityDateDebut, OffsetDateTime activityDateFin) {
            return ((activityDateDebut.isAfter(this.startDate) && activityDateDebut.isEqual(this.startDate)) ||
                    (activityDateFin.isBefore(this.endDate) && activityDateFin.isEqual(this.endDate)));
        }
    */
    public void removeActivity(Activity activity) {
        for (Activity activity1 : activities) {
            if (activity1.getId().equals(activity.getId())) {
                this.activities.remove(activity1);
                activity1.setVacation(null);
                return;
            }
        }
    }

    public boolean userHasAlreadyAtciviteForDateTimeInterval(User user,
                                                             OffsetDateTime activityDateDebut,
                                                             OffsetDateTime activityDateFin) {
        for (Activity activity : this.activities) {
            if (activity.isActivityCrossed(activityDateDebut, activityDateFin) && activity.userIsInside(user))
                return true;
        }
        return false;
    }
    public boolean hasConflictWithExistingActivities(User user,
                                                     OffsetDateTime activityStartDate,
                                                     OffsetDateTime activityEndDate) {
        for (Activity activity : this.activities) {
            if (activity.isConflicting(activityStartDate, activityEndDate) && activity.userIsInside(user)) {
                return true; // Il y a un conflit avec une activité existante
            }
        }
        return false; // Aucun conflit trouvé
    }


    public boolean userIsAlreadyInHoliday() {
        return OffsetDateTime.now().isAfter(this.startDate) && OffsetDateTime.now().isBefore(endDate);
    }

    public void setVacationStatus() {
        OffsetDateTime now = OffsetDateTime.now();

        if (endDate.isBefore(now)) {
            this.status = "Passé";
        } else if ((startDate.isBefore(now) || startDate.isEqual(now)) && (endDate.isAfter(now) || endDate.isEqual(now))) {
            status = "Present";
        } else if (startDate.isAfter(now)) {
            status = "Future";
        } else {
            status = "Unknown";
        }
    }

    public void addParticipantFromVacation(Participant participant) {
        if (!participants.contains(participant)) {
            participants.add(participant);
        }
    }
    public void removeParticipant(Long idParticipant) {
        participants.removeIf(p -> p.getId().equals(idParticipant));
    }
}
