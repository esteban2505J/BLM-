package com.microservice.user.utils;
import com.microservice.user.persitence.model.entities.RoleEntity;
import com.microservice.user.persitence.model.enums.Role;
import com.microservice.user.persitence.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SpringSecurityUtils {

    RoleRepository roleRepository;



    public boolean canYouCreteRole(Role role) {



        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> currentUserRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return currentUserRoles.contains(role.name());

    }

    public boolean canManageThisUser(List<RoleEntity> rolesUserBeDeleted){
//        if(roleName.isBlank()) throw new IllegalArgumentException("Role name cannot be blank");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<String> currentUserRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        int maxUserRoleLevel = currentUserRoles
                .stream()
                .map(role -> roleRepository.findByName(role).orElseThrow(()-> new IllegalArgumentException("Role doesn't exist")).getHierarchyLevel())
               .max(Integer::compare).orElse(0);


        // Get the hierarchy level of the target role to be deleted
        int targetRoleLevel = rolesUserBeDeleted
                .stream()
                .map(RoleEntity::getHierarchyLevel)
                .max(Integer::compare).get();


        // Compare levels: the user must have an equal or higher role level
        return maxUserRoleLevel >= targetRoleLevel;


    }
}
