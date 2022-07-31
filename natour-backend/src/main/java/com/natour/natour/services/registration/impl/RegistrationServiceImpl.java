package com.natour.natour.services.registration.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natour.natour.model.ApplicationUser;
import com.natour.natour.model.SomeSortOfUser;
import com.natour.natour.repositories.ApplicationUserRepository;
import com.natour.natour.services.registration.RegistrationService;

import lombok.extern.java.Log;

@Log
@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;
    
    @Override
    public boolean register(SomeSortOfUser user) {
        ApplicationUser appUser = new ApplicationUser();
        BeanUtils.copyProperties(user, appUser);

        appUser = applicationUserRepository.save(appUser);

        log.info("A new user was successfully registered: " + appUser);
        return true;
    }
}
