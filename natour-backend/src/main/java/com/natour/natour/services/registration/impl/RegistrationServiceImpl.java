package com.natour.natour.services.registration.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natour.natour.model.dto.UserDto;
import com.natour.natour.model.entity.ApplicationUser;
import com.natour.natour.services.registration.RegistrationService;
import com.natour.natour.services.user.ApplicationUserService;

import lombok.extern.java.Log;

@Log
@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private ApplicationUserService applicationUserService;
    
    @Override
    public boolean register(UserDto user) {
        ApplicationUser appUser = new ApplicationUser();
        BeanUtils.copyProperties(user, appUser);

        appUser = applicationUserService.save(appUser);

        log.info("A new user was successfully registered: " + appUser);
        return true;
    }
}
