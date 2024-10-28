package org.helmo.HolyD.domains.responses;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthResponse {
    private long id;
    private String jwtToken;
    private String type = "Bearer";
    private String username;
    private String email;
    private String pictureUrl;
    private String lastname;
    private String firstname;
    private List<String> roles;
}