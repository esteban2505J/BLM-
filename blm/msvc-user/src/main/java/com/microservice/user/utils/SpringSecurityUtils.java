package com.microservice.user.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SpringSecurityUtils {

    // Get the authentication object from the SecurityContext
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    public boolean isUserAuthenticated() {
        // Check if the user is authenticated
        return authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken);
    }
}
