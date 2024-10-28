package org.helmo.HolyD.domains.entities;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
@Table(name = "helmo_users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 30, message = "Wrong lastname size min=2 max=30")
    @Column(length = 30, nullable = false, name = "nom")
    private String lastname;
    @Size(min = 2, max = 30, message = "Wrong firstname size min=2 max=30")
    @Column(length = 30, nullable = false, name = "prenom")
    private String firstname;

    @NotBlank
    @Email
    @Size(min = 2, max = 100, message = "Wrong email size min=2 max=100")
    @Column(length = 100, unique = true, nullable = false)
    private String email;
    @NotBlank
    @Size(max = 120, message = "Wrong password size max=100")
    @Column(length = 100, nullable = false, name = "mot_de_ passe")
    private String passwd;
    @JoinColumn
    private String pictureUrl;

    @OneToMany(mappedBy = "userEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<RoleEntity> roleEntity = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<VacationEntity> vacationEntities = new ArrayList<>();

    @OneToMany(mappedBy = "sender",
            cascade={CascadeType.REMOVE},
            orphanRemoval=true)
    private List<MessageEntity> messageEntities = new ArrayList<>();


    public boolean userIsAlreadyInHoliday(OffsetDateTime dateDebut, OffsetDateTime dateFin){
        return vacationEntities.stream()
                .anyMatch((holiD) ->
                        ((holiD.getStartDate().isBefore(dateFin) || holiD.getStartDate().isEqual(dateFin)) &&
                                (holiD.getEndDate().isAfter(dateDebut) || holiD.getEndDate().isEqual(dateDebut))));
    }
    // getters and setters
}