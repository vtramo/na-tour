package com.natour.natour.services.authentication.jwt.keycloak.admin;


public interface KeyCloakAdminService {
    boolean saveUser(String username, String password);
}
