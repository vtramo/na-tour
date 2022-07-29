package com.natour.natour.services.authentication.facebook.apigraph.impl;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.natour.natour.model.ApplicationUser;
import com.natour.natour.services.authentication.facebook.apigraph.FacebookApiGraphService;
import com.natour.natour.services.authentication.facebook.objects.FacebookUser;
import com.natour.natour.services.authentication.facebook.objects.FacebookUserId;

import lombok.extern.java.Log;

@Log
@Service
public class FacebookApiGraphServiceImpl implements FacebookApiGraphService {

    private static final String URL_GRAPH_API = "https://graph.facebook.com/";
    private static final String USER_DETAILS = "fields=id,name,email";

    private RestTemplate restTemplate;
    public FacebookApiGraphServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public String getUserIdFromAccessToken(String accessToken) {
        final String getUserIdUrl = URL_GRAPH_API + "/me?access_token=" + accessToken;

        ResponseEntity<FacebookUserId> response = 
            buildGetForEntityRequest(getUserIdUrl, FacebookUserId.class);
        
        FacebookUserId fbUserId = response.getBody();
        return fbUserId.getId();
    }   

    @Override
    public ApplicationUser buildApplicationUserFromAccessToken(
     String accessToken, String facebookUserId) {

        final String getUserDetailsUrl = URL_GRAPH_API + "/" + facebookUserId + 
                                                         "?" + USER_DETAILS   +
                                            "&access_token=" + accessToken;

        ResponseEntity<FacebookUser> response = 
            buildGetForEntityRequest(getUserDetailsUrl, FacebookUser.class);

        FacebookUser fbUser = response.getBody();
        return new ApplicationUser(
            null, 
            fbUser.getEmail(),
            fbUser.getEmail(), 
            fbUser.getId()
        );
    }

    private <T> ResponseEntity<T> buildGetForEntityRequest(String url, Class<T> type) {
        try {
            return restTemplate.getForEntity(url, type);
        } catch (RestClientException e) {
            log.warning("Facebook Api Graph error: " + e.getLocalizedMessage());
            throw new RuntimeException();
        }
    }
}
