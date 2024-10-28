package org.helmo.HolyD.domains.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class TokenRequest {
    private String idToken;

}
