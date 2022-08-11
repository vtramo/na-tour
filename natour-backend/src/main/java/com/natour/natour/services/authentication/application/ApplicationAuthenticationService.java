package com.natour.natour.services.authentication.application;

import com.natour.natour.model.dto.CredentialsDto;
import com.natour.natour.model.entity.ApplicationUser;

public interface ApplicationAuthenticationService {

    ApplicationUser authenticate(CredentialsDto credentials);
}
