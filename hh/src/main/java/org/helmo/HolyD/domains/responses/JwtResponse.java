package org.helmo.HolyD.domains.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private long id;
    private String jwtToken;
    private String type = "Bearer";
    private String username;
    private String email;
    private List<String> roles;

}
