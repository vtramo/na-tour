package com.natour.natour.services.authentication.facebook.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natour.natour.model.entity.ApplicationUser;
import com.natour.natour.services.authentication.facebook.FacebookAuthenticationService;
import com.natour.natour.services.authentication.facebook.apigraph.FacebookApiGraphService;
import com.natour.natour.services.user.ApplicationUserService;

import lombok.extern.java.Log;

@Log
@Service
public class FacebookAuthenticationServiceImpl implements FacebookAuthenticationService {

    @Autowired
    private FacebookApiGraphService facebookApiGraphService;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Override
    public ApplicationUser authenticate(String accessToken) {
        String fbUserId = facebookApiGraphService.getUserIdFromAccessToken(accessToken);
        
        ApplicationUser user = 
            facebookApiGraphService.buildApplicationUserFromAccessToken(accessToken, fbUserId);
        
        return saveUserIfNotExist(user);
    }
    
    private ApplicationUser saveUserIfNotExist(ApplicationUser user) {
        if (applicationUserService.findByUsername(user.getEmail()) == null) {
            user = applicationUserService.save(user);
            
            log.info("A new Facebook User has been created: " + user);
        }
        return user;
    }
}
