package org.helmo.HolyD.domains.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class LoginDTO {
    private String username;
    private String password;
}
