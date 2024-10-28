package org.helmo.HolyD.domains.requests;


import lombok.*;
import org.helmo.HolyD.domains.models.Activity;
import org.helmo.HolyD.domains.models.Schedule;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private ScheduleDTO scheduleDTO;
    private ActivityDTO activityDTO;

}
