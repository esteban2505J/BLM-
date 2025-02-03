package com.microservice.user.utils;


import com.microservice.user.persitence.model.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import java.util.stream.Collectors;

@Service
public class SpringSecurityUtils {

    SpringSecurityUtils springSecurityUtils;


    // Get the authentication object from the SecurityContext
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    public boolean isUserAuthenticated() {
        // Check if the user is authenticated
        return authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken);
    }
    public boolean canYouCreteRole(Role role) {
        // Get the current user's roles
        // Get the current user's roles (Authorities)
        Set<String> currentUserRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)  // This maps to the role/authority string (e.g., "ROLE_ADMIN")
                .collect(Collectors.toSet());

        return currentUserRoles.contains(role.name());

    }
}
