package com.natour.natour.services.authentication.jwt.keycloak;

import java.util.List;
import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.natour.natour.model.Token;
import com.natour.natour.services.authentication.jwt.TokenGeneratorService;
import com.natour.natour.services.authentication.jwt.TokenScope;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.java.Log;

@Service
@Log
public class KeyCloakTokenGeneratorService implements TokenGeneratorService {

    @Data
    @EqualsAndHashCode(callSuper=true)
    public static class KeyCloakToken extends Token {  
        @JsonProperty("expires_in") 
        long expiresIn;

        @JsonProperty("refresh_expires_in") 
        long refreshExpiresIn;

        @JsonProperty("token_type") 
        String tokenType;

        @JsonProperty("not_before_policy")
        int notBeforePolicy;

        @JsonProperty("session_state")
        String sessionState;
        
        @JsonProperty("scope")
        String scope;

        KeyCloakToken(String accessToken, String refreshToken) {
            super(accessToken, refreshToken);
        }
    }

    private final RestTemplate restTemplate;
    public KeyCloakTokenGeneratorService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    private KeyCloakTokenScope scope;

    @Override
    public Token generateToken(Map<String, List<String>> params, TokenScope tokenScope) {
        scope = KeyCloakTokenScope.convertScope(tokenScope);
            
        HttpEntity<MultiValueMap<String, String>> request = buildRequestForToken(params);
        ResponseEntity<KeyCloakToken> response = sendRequestForToken(request);
           
        KeyCloakToken keyCloakToken = response.getBody();
        log.info("KeyCloakToken successfully generated with scope: " + scope 
                    + " " + keyCloakToken);

        return keyCloakToken;
    }

    private HttpEntity<MultiValueMap<String, String>> buildRequestForToken(
     Map<String, List<String>> params) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formVariables = new LinkedMultiValueMap<>(
            scope.getParameters(params)
        );

        return new HttpEntity<>(formVariables, headers);
    }

    private ResponseEntity<KeyCloakToken> sendRequestForToken(
     HttpEntity<MultiValueMap<String, String>> request) {

        ResponseEntity<KeyCloakToken> response = null;
        try {
            response = restTemplate.postForEntity(
                scope.urlTokenKey,
                request,
                KeyCloakToken.class
            );
        } catch (RestClientException e) {
            log.warning("KeyCloakTokenRequest error. Scope: " + scope + " " + e.getLocalizedMessage());
            throw new RuntimeException();
        }
        return response;
    }
}