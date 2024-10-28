package org.helmo.HolyD.services.inter_service;


import org.helmo.HolyD.domains.requests.SignupDTO;
import org.helmo.HolyD.domains.responses.AuthResponse;
import org.helmo.HolyD.domains.responses.MessageResponse;

public interface IUserService {
    AuthResponse createUserOAuth(String userId, String firstname, String lastname, String email);

    AuthResponse loginNewUser(String username, String password);
   // AuthResponse loginNewUser(String username, String rawPassword,String pictureUrl);

    MessageResponse createUser(SignupDTO request);
}

