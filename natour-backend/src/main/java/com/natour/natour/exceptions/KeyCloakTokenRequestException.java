package com.natour.natour.exceptions;

import org.springframework.http.ResponseEntity;

import com.natour.natour.services.authentication.jwt.keycloak.KeyCloakTokenScope;
import com.natour.natour.services.authentication.jwt.keycloak.KeyCloakTokenGeneratorService.KeyCloakToken;

import lombok.extern.java.Log;

@Log
public class KeyCloakTokenRequestException extends RuntimeException {

    public static void throwException(
     ResponseEntity<KeyCloakToken> response, KeyCloakTokenScope scope) {

        log.warning("Unsuccessful KeyCloakToken request. Scope: " + scope + ". " + 
        "Response HTTP Status: " + response.getStatusCode() + ". " + 
        "Response body: " + response.getBody().toString());

        throw new KeyCloakTokenRequestException();
    }
}
