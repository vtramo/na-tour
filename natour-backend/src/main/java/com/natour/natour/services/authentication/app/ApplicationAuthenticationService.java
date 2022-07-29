package com.natour.natour.services.authentication.app;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natour.natour.model.ApplicationUser;
import com.natour.natour.model.AuthenticationResponse;
import com.natour.natour.model.Credentials;
import com.natour.natour.model.Token;
import com.natour.natour.repositories.ApplicationUserRepository;
import com.natour.natour.services.authentication.AuthenticationService;
import com.natour.natour.services.authentication.jwt.TokenGeneratorService;
import com.natour.natour.services.authentication.jwt.TokenScope;

import lombok.extern.java.Log;

@Log
@Service
public class ApplicationAuthenticationService implements AuthenticationService {
    
    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private TokenGeneratorService tokenGeneratorService;

    @Override
    public AuthenticationResponse authenticate(Credentials credentials) {
        Objects.requireNonNull(credentials);

        ApplicationUser user = login(credentials);

        Token token = tokenGeneratorService.generateToken(Map.of(
            "username", List.of(credentials.getUsername()),
            "password", List.of(credentials.getPassword())
        ), TokenScope.USER);

        return new AuthenticationResponse(true, user, token);
    }

    private ApplicationUser login(Credentials credentials) {
        ApplicationUser user = 
            applicationUserRepository.findByUsername(credentials.getUsername());

        if (credentials.getPassword().equals(user.getPassword())) return user;
        else {
            log.warning("Login failed. The credentials are invalid: " + credentials);
            throw new IllegalArgumentException("The credentials are invalid.");
        }
    }
}
