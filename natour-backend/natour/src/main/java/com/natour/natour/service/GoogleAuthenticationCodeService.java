package com.natour.natour.service;

import org.springframework.stereotype.Service;

@Service
public class GoogleAuthenticationCodeService {

    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";

    private final String authenticationCode;

    public GoogleAuthenticationCodeService(String authenticationCode) {
        this.authenticationCode = authenticationCode;
    }

    public GoogleAuthenticationCodeService() {
        this.authenticationCode = null;
    }

}