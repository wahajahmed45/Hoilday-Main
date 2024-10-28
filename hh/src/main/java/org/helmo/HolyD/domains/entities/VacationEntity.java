package org.helmo.HolyD.domains.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "helmo_vacation")
public class VacationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 50, message = "Wrong name size min=2 max=50")
    @Column(length = 50, nullable = false)
    private String name;
    @Size(min = 2, max = 250, message = "Wrong description size min=2 max=250")
    @Column(length = 250, nullable = false)
    private String description;

    @Column(nullable = false)
    private OffsetDateTime startDate;
    @Column(nullable = false)
    private OffsetDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @JoinColumn
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private PlaceEntity placeEntity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "helmo_vacation_participant",
            joinColumns = {@JoinColumn(name = "vacation_id")},
            inverseJoinColumns = {@JoinColumn(name = "participant_id")})
    private List<ParticipantEntity> participantEntities = new ArrayList<>();

    @OneToMany(mappedBy = "vacationEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityEntity> activityEntities = new ArrayList<>();

    @OneToMany(mappedBy = "vacationEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentEntity> documentEntities = new ArrayList<>();

   /* @JsonIgnore
    @Transient
    List<UserEntity> userEntities = new ArrayList<>();*/

    /*  @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Vacance")
        @SequenceGenerator(name = "id_Vacance", sequenceName = "ID_VACANCE", allocationSize = 1)
        private Long id;*/
    @OneToMany(mappedBy = "vacance", cascade = {CascadeType.ALL}, orphanRemoval = true)
    // Bug si non All (veut inserer null dans content alors que content n'est pas vide)
    private List<MessageEntity> messages = new ArrayList<>();

    /*  @ManyToMany(fetch = FetchType.EAGER)
      private List<ParticipantEntity> participantEntities = new ArrayList<>();*/
    /*public boolean addParticipant(UserEntity userEntity) {
        if (!this.userEntities.contains(userEntity)) {
            this.userEntities.add(userEntity);
            return true;
        } else {
            return false;
        }
    }*/

    public void addMessage(MessageEntity messageEntity, UserEntity userEntity) {
        messageEntity.setSendingDate(OffsetDateTime.now());
        messageEntity.setSender(userEntity);
        messageEntity.setVacance(this);
        this.messages.add(messageEntity);
    }

    public boolean intervalIsInside(OffsetDateTime dateDebut, OffsetDateTime dateFin) {
        return ((dateDebut.isAfter(this.startDate) || dateDebut.isEqual(this.startDate)) &&
                (dateFin.isBefore(this.endDate) || dateFin.isEqual(this.endDate)));
    }

    public boolean userHasAlreadyAtciviteForDateTimeInterval(UserEntity userEntity,
                                                             OffsetDateTime dateDebut, OffsetDateTime dateFin) {
        for (ActivityEntity acti : this.activityEntities) {
            if (acti.intervalIsCrossed(dateDebut, dateFin) && acti.userIsInside(userEntity))
                return true;
        }
        return false;
    }

    public boolean userHasAlreadyAtciviteForDateTimeIntervalWithOutOneActi(UserEntity userEntity, OffsetDateTime dateDebut, OffsetDateTime dateFin, Long idActivite) {
        for (ActivityEntity acti : this.activityEntities) {
            if (Objects.equals(acti.getId(), idActivite) && acti.intervalIsCrossed(dateDebut, dateFin) && acti.userIsInside(userEntity))
                return true;
        }
        return false;
    }

    public ActivityEntity editDateOfActivity(Long id, OffsetDateTime dateDebut, OffsetDateTime dateFin) {
        for (ActivityEntity acti : this.activityEntities) {
            if (Objects.equals(acti.getId(), id)) {
                acti.setStartDate(dateDebut);
                acti.setEndDate(dateFin);
                return acti;
            }

        }
        return null;
    }

}
