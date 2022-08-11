package com.natour.natour.services.registration;

import com.natour.natour.model.dto.UserDto;

public interface RegistrationService {
    boolean register(UserDto user);
}
