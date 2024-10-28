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
public class User {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String pictureUrl;
    private List<Role> roles = new ArrayList<>();
    private List<Vacation> vacations = new ArrayList<>();

    public boolean userIsAlreadyInHoliday(OffsetDateTime dateDebut, OffsetDateTime dateFin) {
        return vacations.stream()
                .anyMatch((holiday) ->
                        ((holiday.getStartDate().isBefore(dateFin) || holiday.getStartDate().isEqual(dateFin)) &&
                                (holiday.getEndDate().isAfter(dateDebut) || holiday.getEndDate().isEqual(dateDebut))));
    }

}
