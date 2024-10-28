package org.helmo.HolyD.domains.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
@Entity
@Table(name = "helmo_activity")
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 50, message = "Wrong name  size min=2 max=50")
    @Column(length = 50, nullable = false)
    private String name;
    @Size(min = 2, max = 50, message = "Wrong description size min=2 max=50")
    @Column(length = 50, nullable = false)
    private String description;
    @Column(nullable = false)
    private OffsetDateTime startDate;
    @Column(nullable = false)
    private OffsetDateTime endDate;
    @Column
    private String status;
   /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)*/
    @Column(name = "user_id", nullable = false)
    private Long userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    //All because Persist is Bugged for this one (vacance and lieu transient works with PERSIST)
    @JoinColumn(name = "place_id", nullable = false)
    private PlaceEntity placeEntity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacation_id")
    private VacationEntity vacationEntity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "helmo_activity_participant",
            joinColumns = {@JoinColumn(name = "activity_id")},
            inverseJoinColumns = {@JoinColumn(name = "participant_id")})
    private List<ParticipantEntity> participantEntities = new ArrayList<>();


    public boolean intervalIsCrossed(OffsetDateTime dateDebut, OffsetDateTime dateFin) {
        return ((this.startDate.isBefore(dateFin) || this.startDate.isEqual(dateFin)) &&
                (this.endDate.isAfter(dateDebut) || this.endDate.isEqual(dateDebut)));
    }

    public boolean userIsInside(UserEntity userEntity) {
        return this.participantEntities.contains(userEntity);
    }

    public void removeAllParticipant() {
        this.participantEntities.clear();
    }
}