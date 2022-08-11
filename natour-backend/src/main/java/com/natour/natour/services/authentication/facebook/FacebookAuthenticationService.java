package com.natour.natour.services.authentication.facebook;

import com.natour.natour.model.entity.ApplicationUser;

public interface FacebookAuthenticationService {
    ApplicationUser authenticate(String accessToken);
}
