package org.helmo.HolyD.domains.requests;


import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class ActivityDTO {
    private Long id;
    @Size(min = 2, max = 50, message = "Wrong name size min=2 max=50")
    private String nom;
    @Size(min = 2, max = 250, message = "Wrong description size min=2 max=250")
    private String description;
    private OffsetDateTime dateDebut;
    private OffsetDateTime dateFin;
    private PlaceDTO placeDTO;
    private String status;
    private Long user;
    private VacationDTO vacationDTO;
    private List<ParticipantDTO> participants = new ArrayList<>();
}
