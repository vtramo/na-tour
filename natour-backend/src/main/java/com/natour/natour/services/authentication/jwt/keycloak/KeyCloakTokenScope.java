package com.natour.natour.services.authentication.jwt.keycloak;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;

import com.natour.natour.services.authentication.jwt.TokenScope;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum KeyCloakTokenScope {
    USER(
        "natour", 
        "password", 
        Strings.EMPTY,
        "http://192.168.1.4:8180/realms/master/protocol/openid-connect/token"),

    ADMIN(
        "admin-cli", 
        "client_credentials", 
        "bxCcwf8KjT2A9UKPV8OQixaEOYlIjYiT",
        "http://192.168.1.4:8180/realms/master/protocol/openid-connect/token");

    private final String clientId, grantType, clientSecret;
    final String  urlTokenKey;

    Map<String, List<String>> getParameters(Map<String, List<String>> addionalParameters) {
        Map<String, List<String>> map = new HashMap<>(addionalParameters);

        map.put("client_id", List.of(clientId));
        map.put("grant_type", List.of(grantType));
        if (clientSecret != Strings.EMPTY) map.put("client_secret", List.of(clientSecret));

        return map;
    }

    static KeyCloakTokenScope convertScope(TokenScope tokenPermissions) {
        switch (tokenPermissions) {
            case ADMIN:
                return KeyCloakTokenScope.ADMIN;
            default:
                return KeyCloakTokenScope.USER;
        }
    }
}
