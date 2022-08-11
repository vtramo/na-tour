package com.natour.natour.services.authentication.google;

import com.natour.natour.model.entity.ApplicationUser;

public interface GoogleAuthenticationService {
    ApplicationUser authenticate(String authCode);
}
