package org.helmo.HolyD.domains.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class Role {
    private Long id;
    private ERole name;
}
