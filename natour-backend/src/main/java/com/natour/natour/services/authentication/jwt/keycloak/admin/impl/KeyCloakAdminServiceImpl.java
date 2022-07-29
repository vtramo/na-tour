package com.natour.natour.services.authentication.jwt.keycloak.admin.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.natour.natour.model.Token;
import com.natour.natour.services.authentication.jwt.TokenScope;
import com.natour.natour.services.authentication.jwt.keycloak.KeyCloakTokenGeneratorService;
import com.natour.natour.services.authentication.jwt.keycloak.admin.KeyCloakAdminService;

import lombok.Data;

@Service
public class KeyCloakAdminServiceImpl implements KeyCloakAdminService {

    private static final String URL_SAVE_USER = "http://192.168.1.4:8180/admin/realms/master/users";

    private static final String CREDENTIALS_TYPE = "password";
    
    @Data
    private static class KeyCloakUser implements Serializable {  
        @JsonProperty("enabled") 
        boolean enabled = true;

        @JsonProperty("credentials")
        List<KeyCloakUserCredentials> credentials;

        @JsonProperty("username") 
        String username;

        KeyCloakUser(List<KeyCloakUserCredentials> credentials, String username) { 
            this.credentials = credentials;
            this.username = username; 
        }
    }

    @Data
    private static class KeyCloakUserCredentials implements Serializable {
        @JsonProperty("type")
        String type = CREDENTIALS_TYPE;

        @JsonProperty("temporary")
        boolean temporary = false;

        @JsonProperty("value")
        String value;

        KeyCloakUserCredentials(String value) { this.value = value; }
    }

    @Autowired
    private KeyCloakTokenGeneratorService keyCloakTokenGeneratorService;

    private final RestTemplate restTemplate;
    public KeyCloakAdminServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
    
    @Override
    public boolean saveUser(String username, String password) {
        KeyCloakUser user = createKeyCloakUser(username, password);
        String userJSON = convertKeyCloakUserToJSON(user);

        HttpEntity<String> request = buildRequestForSaveUser(userJSON);
        ResponseEntity<Void> response = 
            restTemplate.postForEntity(URL_SAVE_USER, request, null);

        return response.getStatusCode() == HttpStatus.CREATED;
    }

    private KeyCloakUser createKeyCloakUser(String username, String password) {
        KeyCloakUserCredentials credentials = new KeyCloakUserCredentials(password);
        return new KeyCloakUser(List.of(credentials), username);
    }

    private String convertKeyCloakUserToJSON(KeyCloakUser user) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(user);   
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Conversion KeyCloakUser to JSON failed");
        }
    }

    private HttpEntity<String> buildRequestForSaveUser(String userJSON) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Token adminToken = keyCloakTokenGeneratorService.generateToken(Map.of(), TokenScope.ADMIN);
        headers.setBearerAuth(adminToken.getAccessToken());
        return new HttpEntity<String>(userJSON, headers);
    }
}
