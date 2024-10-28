package org.helmo.HolyD.domains.models;

import jakarta.persistence.*;
import lombok.*;
import org.helmo.HolyD.domains.entities.UserEntity;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class Schedule {
    private Long id;
    private String title;
    private User user;
}
