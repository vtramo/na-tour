package com.natour.natour.services.authentication.google;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import lombok.extern.java.Log;

@Log
@Service
public class GoogleAuthCodeService {

    private static final String CLIENT_ID = "470319492727-viq0pqpsdd33d6vr4vsdc78cusco9g9l.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String URL_GOOGLEAPIS_TOKEN = "https://oauth2.googleapis.com/token";
    private static final String REDIRECT_URI = "http://192.168.56.1.nip.io:8080/login/oauth2/code/google";

    public GoogleIdToken getIdToken(String authCode) {
        GoogleAuthorizationCodeTokenRequest tokenRequest =
            buildAuthorizationCodeTokenRequest(authCode);

        return executeGoogleTokenRequest(tokenRequest);
    }

    private GoogleAuthorizationCodeTokenRequest buildAuthorizationCodeTokenRequest(String authCode) {
        return new GoogleAuthorizationCodeTokenRequest(
            new NetHttpTransport(),
            JacksonFactory.getDefaultInstance(),
            URL_GOOGLEAPIS_TOKEN,
            CLIENT_ID,
            CLIENT_SECRET,
            authCode,
            REDIRECT_URI
        );
    }

    private GoogleIdToken executeGoogleTokenRequest(GoogleAuthorizationCodeTokenRequest request) {
        GoogleTokenResponse tokenResponse;
        try {
            tokenResponse = request.execute();
            return tokenResponse.parseIdToken();
        } catch (Exception e) {
            log.warning("Google authcode token request failed: " + e.getMessage());
            throw new RuntimeException("Google authcode token request failed.");
        }
    }
}