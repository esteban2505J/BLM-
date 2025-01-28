package com.microservice.user.service.implementation;

import com.microservice.user.persitence.model.entities.PermissionEntity;
import com.microservice.user.persitence.model.entities.RoleEntity;
import com.microservice.user.persitence.model.enums.Role;
import com.microservice.user.persitence.repository.PermissionRepository;
import com.microservice.user.persitence.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RolesService {

    private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;
    public RolesService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public RoleEntity createRoleDefaultPermission(Role roleName) {
        // Buscar los permisos predeterminados para este rol
        Set<PermissionEntity> defaultPermissions = getDefaultPermissionsForRole(roleName);

        // Crear el rol con los permisos predeterminados
        RoleEntity role = RoleEntity.builder()
                .name(roleName)
                .permissions(defaultPermissions)
                .build();

        // Guardar el rol en la base de datos
        return roleRepository.save(role);
    }

    // Método para obtener permisos predeterminados
    private Set<PermissionEntity> getDefaultPermissionsForRole(Role roleName) {
        Set<PermissionEntity> defaultPermissions = new HashSet<>();
        switch (roleName) {
            case ADMIN:
                defaultPermissions.addAll(permissionRepository.findByNameIn(List.of("CREATE_USER", "DELETE_USER", "VIEW_USER")));
                break;
            case CLIENT:
                defaultPermissions.addAll(permissionRepository.findByNameIn(List.of("VIEW_PROFILE", "EDIT_PROFILE")));
                break;
            case AUX:
                defaultPermissions.addAll(permissionRepository.findByNameIn(List.of("VIEW_CATALOG")));
                break;
            default:
                throw new IllegalArgumentException("Invalid role name: " + roleName);
        }
        return defaultPermissions;
    }




}
