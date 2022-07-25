package com.natour.natour.security.authproviders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.natour.natour.model.ApplicationUser;
import com.natour.natour.repository.ApplicationUserRepository;

import lombok.extern.java.Log;

@Log
@Order(1)
@Component
public class DefaultAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    private static class ProvidedCredentials {
        String username, password;

        boolean areValid(ApplicationUser user) {
            return user.getUsername().equals(username) && 
                   user.getPassword().equals(password);
        }
    }
    private final ProvidedCredentials credentials = new ProvidedCredentials();
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        credentials.username = authentication.getName();
        credentials.password = authentication.getCredentials().toString();

        log.info("FIND USER BY USERNAME");

        ApplicationUser user = findUserByUsername(credentials.username);
        
        log.info("USER FOUND - VALID?");

        return credentials.areValid(user)
            ? createAuthenticationToken()
            : null;
    }

    private ApplicationUser findUserByUsername(String username) {
        ApplicationUser user = applicationUserRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("Username doesn't exist");
        return user;
    }

    private Authentication createAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(credentials.username, credentials.password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
