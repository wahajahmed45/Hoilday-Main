package org.helmo.HolyD.domains.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
@Entity
@Table(name = "helmo_place")
public class PlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = -180, message = "Wrong longitude size min=-180")
    @Max(value = 180, message = "Wrong longitude size max=180")
    @Column(scale = 6, precision = 9, nullable = false)
    private BigDecimal longitude;
    @Min(value = -90, message = "Wrong latitude size max=90")
    @Max(value = 90, message = "Wrong latitude size max=90")
    @Column(scale = 6, precision = 8, nullable = false)
    private BigDecimal latitude;

    @Size(max = 70, message = "Wrong street size max=70")
    @Column(length = 70, nullable = true)
    private String street;

    @Size(max = 15, message = "Wrong street number size max=15")
    @Column(length = 15, nullable = true)
    private String number;

    @Size(max = 15, message = "Wrong postal code size max=15")
    @Column(length = 15, nullable = true)
    private String postalCode;

    @Size(max = 50, message = "Wrong city size max=50")
    @Column(length = 50, nullable = true)
    private String city;
    @Size(min = 2, max = 50, message = "Wrong country size min=2 max=50")
    @Column(length = 50, nullable = false)
    private String country;

    @OneToMany(mappedBy = "placeEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<VacationEntity> vacationEntities = new ArrayList<>();

    @OneToMany(mappedBy = "placeEntity",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<ActivityEntity> activityEntities = new ArrayList<>();

    public void removerActivity(Long id) {
        activityEntities.removeIf(activityEntity -> activityEntity.getId().equals(id));
    }
}
