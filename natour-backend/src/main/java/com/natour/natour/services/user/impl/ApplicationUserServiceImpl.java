package com.natour.natour.services.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natour.natour.model.entity.ApplicationUser;
import com.natour.natour.repositories.ApplicationUserRepository;
import com.natour.natour.services.authentication.jwt.keycloak.admin.KeyCloakAdminService;
import com.natour.natour.services.user.ApplicationUserService;

@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {

    @Autowired
    private KeyCloakAdminService keyCloakAdminService;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Override
    public ApplicationUser save(ApplicationUser user) {
        keyCloakAdminService.saveUser(user.getUsername(), user.getPassword());
        return applicationUserRepository.save(user);
    }

    @Override
    public ApplicationUser findByUsername(String username) {
        return applicationUserRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return applicationUserRepository.existsByUsername(username);
    }
}
