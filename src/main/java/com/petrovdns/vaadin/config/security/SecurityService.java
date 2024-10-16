package com.petrovdns.vaadin.config.security;

import com.petrovdns.vaadin.data.entity.UserEntity;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;

    @Autowired
    public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    public UserDetails getAuthenticatedUser() {
        if (authenticationContext.getAuthenticatedUser(UserDetails.class).isPresent()) {
            return authenticationContext.getAuthenticatedUser(UserDetails.class).get();
        }
        return null;
    }

    public void logout() {
        authenticationContext.logout();
    }

}