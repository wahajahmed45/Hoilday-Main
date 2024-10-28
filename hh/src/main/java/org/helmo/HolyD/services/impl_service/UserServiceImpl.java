package org.helmo.HolyD.services.impl_service;


import org.helmo.HolyD.domains.mapper.dtoMapper.IUserMapper;
import org.helmo.HolyD.domains.models.ERole;
import org.helmo.HolyD.domains.models.Role;
import org.helmo.HolyD.domains.models.User;
import org.helmo.HolyD.domains.requests.SignupDTO;
import org.helmo.HolyD.domains.responses.AuthResponse;
import org.helmo.HolyD.domains.responses.MessageResponse;
import org.helmo.HolyD.repositories.dao.inter.IUserDAO;
import org.helmo.HolyD.securities.jwt.JwtUtils;
import org.helmo.HolyD.securities.service.UserDetailsImpl;
import org.helmo.HolyD.services.inter_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IUserDAO userDAO;

    private final IUserMapper USER_MAPPER = IUserMapper.INSTANCE;

    @Override
    public AuthResponse createUserOAuth(String pictureUrl, String firstname, String lastname, String email) {
        // Create new user's account
        String tempPassword = "gdDIABQ4H4";
        User foundUser;
        if (!userDAO.existsByEmail(email)) {
            String password = encoder.encode(tempPassword);
            Role role = Role.builder().name(ERole.ROLE_USER).build();
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            User user = User.builder()
                    .email(email)
                    .firstname(firstname)
                    .lastname(lastname)
                    .password(password)
                    .roles(roles)
                    .pictureUrl(pictureUrl)
                    .build();
            foundUser = userDAO.saveUser(user);

        } else {
            foundUser = userDAO.findUserByEmail(email);
        }
        AuthResponse authResponse = loginNewUser(foundUser.getEmail(), tempPassword);
        return authResponse;
    }

  /*  public AuthResponse loginNewUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return getAuthResponse(authentication);
    }*/

    private AuthResponse getAuthResponse(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User foundUser = userDAO.findUserByEmail(userDetails.getUsername());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        AuthResponse authResponse = AuthResponse.builder()
                .id(userDetails.getId())
                .lastname(foundUser.getLastname())
                .firstname(foundUser.getFirstname())
                .jwtToken(jwt)
                .email(userDetails.getEmail())
                .pictureUrl(foundUser.getPictureUrl())
                .roles(roles)
                .build();
        return authResponse;
    }

    @Override
    public AuthResponse loginNewUser(String email, String tempPassword) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, tempPassword));

        return getAuthResponse(authentication);
    }

    @Override
    public MessageResponse createUser(SignupDTO request) {
        User user = USER_MAPPER.toUser(request);
        if (userDAO.existsByEmail(user.getEmail())) {
            return MessageResponse.builder().message("Email Already Exists").build();

        }
        String passwordEncoded = encoder.encode(user.getPassword());
        user.setPassword(passwordEncoded);
        Role role = Role.builder().name(ERole.ROLE_USER).build();
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        userDAO.saveUser(user);
        return MessageResponse.builder().message("User registered successfully!").build();

    }

}

