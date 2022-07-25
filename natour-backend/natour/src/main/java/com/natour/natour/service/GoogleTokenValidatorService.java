package com.natour.natour.service;

import java.util.Collections;
import org.springframework.stereotype.Service;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import lombok.extern.java.Log;

@Log
@Service
public class GoogleTokenValidatorService {

    private final static String CLIENT_ID = 
        "470319492727-viq0pqpsdd33d6vr4vsdc78cusco9g9l.apps.googleusercontent.com";

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenValidatorService() {
        verifier = buildGoogleIdTokenVerifier();
    }

    private GoogleIdTokenVerifier buildGoogleIdTokenVerifier() {
        return new GoogleIdTokenVerifier
            .Builder(new NetHttpTransport(), new GsonFactory())                                    
            .setAudience(Collections.singletonList(CLIENT_ID))
            .build();
    }

    public final static GoogleIdToken TOKEN_NOT_VALID = null;

    public GoogleIdToken validateToken(String token) {
        GoogleIdToken idToken = TOKEN_NOT_VALID;
        try {
            idToken = verifier.verify(token);
        } catch (Exception e) {
            log.warning(e.toString());
        }
        return idToken;
    }
}
