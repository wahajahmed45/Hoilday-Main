package org.helmo.HolyD.domains.requests;

import lombok.*;
import org.helmo.HolyD.domains.entities.UserEntity;
import org.helmo.HolyD.domains.models.User;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class ScheduleDTO {
    private Long id;
    private String title;
    private User user;
}
