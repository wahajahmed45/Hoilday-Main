package org.helmo.HolyD.domains.requests;


import jakarta.validation.constraints.Size;
import lombok.*;
import org.helmo.HolyD.domains.models.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class VacationDTO {
    private Long id;
    @Size(min = 2, max = 50, message = "Wrong name size min=2 max=50")
    private String name;
    @Size(min = 2, max = 250, message = "Wrong description size min=2 max=250")
    private String description;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private PlaceDTO placeDTO;
    private String status;
    private User owner;
    private List<ParticipantDTO> participants = new ArrayList<>();
    private List<ActivityDTO> activities =  new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private List<DocumentDTO> documents = new ArrayList<>();
}
