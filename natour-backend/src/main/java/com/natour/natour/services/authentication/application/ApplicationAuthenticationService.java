package com.natour.natour.services.authentication.application;

import com.natour.natour.model.ApplicationUser;
import com.natour.natour.model.Credentials;

public interface ApplicationAuthenticationService {

    ApplicationUser authenticate(Credentials credentials);
}
