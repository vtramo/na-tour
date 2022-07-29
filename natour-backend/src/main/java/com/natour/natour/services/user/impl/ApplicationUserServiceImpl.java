package com.natour.natour.services.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natour.natour.model.ApplicationUser;
import com.natour.natour.repositories.ApplicationUserRepository;
import com.natour.natour.services.authentication.jwt.keycloak.admin.KeyCloakAdminService;
import com.natour.natour.services.user.ApplicationUserService;

@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {

    @Autowired
    private KeyCloakAdminService keyCloakAdminApiService;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Override
    public ApplicationUser save(ApplicationUser user) {
        keyCloakAdminApiService.saveUser(user.getUsername(), user.getPassword());
        return applicationUserRepository.save(user);
    }

    @Override
    public ApplicationUser findByUsername(String username) {
        return applicationUserRepository.findByUsername(username);
    }
}