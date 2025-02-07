package com.microservice.user.utils;
import com.microservice.user.persitence.model.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import java.util.stream.Collectors;

@Service
public class SpringSecurityUtils {




    public boolean canYouCreteRole(Role role) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> currentUserRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)  // This maps to the role/authority string (e.g., "ROLE_ADMIN")
                .collect(Collectors.toSet());

        return currentUserRoles.contains(role.name());

    }
}
