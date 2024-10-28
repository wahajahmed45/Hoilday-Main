package org.helmo.HolyD.domains.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class Participant {
    private Long id;
    private String email;
    private String nom;
    private String prenom;

}
