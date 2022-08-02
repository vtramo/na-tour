package com.natour.natour.services.user;

import com.natour.natour.model.ApplicationUser;

public interface ApplicationUserService {
    ApplicationUser save(ApplicationUser user);
    ApplicationUser findByUsername(String username);
    boolean existsByUsername(String username);
}
