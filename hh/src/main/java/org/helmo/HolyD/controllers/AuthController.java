package org.helmo.HolyD.controllers;


import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;


import jakarta.validation.Valid;
import org.helmo.HolyD.domains.requests.LoginDTO;
import org.helmo.HolyD.domains.requests.SignupDTO;
import org.helmo.HolyD.domains.requests.TokenRequest;
import org.helmo.HolyD.domains.responses.AuthResponse;
import org.helmo.HolyD.domains.responses.MessageResponse;
import org.helmo.HolyD.services.inter_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${be.helmo.app.client_Id_web}")
    private String CLIENT_ID;
    @Autowired
    private IUserService userService;


    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        AuthResponse authResponse = userService.loginNewUser(loginDTO.getUsername(), loginDTO.getPassword());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> signUp(@Valid @RequestBody SignupDTO request) {
        MessageResponse messageResponse = userService.createUser(request);
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/oauth")
    public ResponseEntity<?> verifyGoogleToken(@RequestBody TokenRequest tokenRequest) {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(tokenRequest.getIdToken());
            if (idToken != null) {
                AuthResponse authResponse = getAuthResponse(idToken);
                return ResponseEntity.ok(authResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID token");
            }
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token verification failed");
        }
    }

    private AuthResponse getAuthResponse(GoogleIdToken idToken) {
        GoogleIdToken.Payload payload = idToken.getPayload();

        // Récupérer les informations utilisateur
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        String givenName = (String) payload.get("given_name");

        // Créer un JWT

        return createJwtToken(pictureUrl, givenName, name, email);
    }

    private AuthResponse createJwtToken(String pictureUrl, String firstname, String lastname, String email) {

        return userService.createUserOAuth(pictureUrl, firstname, lastname, email);
    }
}
