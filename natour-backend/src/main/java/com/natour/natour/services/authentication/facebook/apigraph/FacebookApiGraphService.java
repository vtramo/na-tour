package com.natour.natour.services.authentication.facebook.apigraph;

import com.natour.natour.model.ApplicationUser;

public interface FacebookApiGraphService {
    String getUserIdFromAccessToken(String accessToken);
    ApplicationUser buildApplicationUserFromAccessToken(String accessToken, String facebookUserId);
}