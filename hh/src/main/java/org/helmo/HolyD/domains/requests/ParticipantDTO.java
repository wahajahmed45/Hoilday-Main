package org.helmo.HolyD.domains.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class ParticipantDTO {
    private Long id;
    private String email;
    private String nom;
    private String prenom;


}
