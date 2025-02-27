package com.microservice.user.persistence.repository;

import com.microservice.user.persistence.model.entities.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    // Buscar permisos por nombre
    List<PermissionEntity> findByNameIn(List<String> names);
}
