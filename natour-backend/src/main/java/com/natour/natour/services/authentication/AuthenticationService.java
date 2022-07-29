package com.natour.natour.services.authentication;

import com.natour.natour.model.AuthenticationResponse;
import com.natour.natour.model.Credentials;

public interface AuthenticationService {
    AuthenticationResponse authenticate(Credentials credentials);
}
