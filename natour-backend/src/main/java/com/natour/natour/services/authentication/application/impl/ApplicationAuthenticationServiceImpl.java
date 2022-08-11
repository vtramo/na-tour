package com.natour.natour.services.authentication.application.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natour.natour.model.dto.Credentials;
import com.natour.natour.model.entity.ApplicationUser;
import com.natour.natour.repositories.ApplicationUserRepository;
import com.natour.natour.services.authentication.application.ApplicationAuthenticationService;

import lombok.extern.java.Log;

@Log
@Service
public class ApplicationAuthenticationServiceImpl implements ApplicationAuthenticationService {
    
    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Override
    public ApplicationUser authenticate(Credentials credentials) {
        Objects.requireNonNull(credentials);

        return login(credentials);
    }

    private ApplicationUser login(Credentials credentials) {
        ApplicationUser user = 
            applicationUserRepository.findByUsername(credentials.getUsername());

        if (credentials.getPassword().equals(user.getPassword())) return user;
        else {
            log.warning("Login failed. The credentials are invalid: " + credentials);
            throw new IllegalArgumentException();
        }
    }
}