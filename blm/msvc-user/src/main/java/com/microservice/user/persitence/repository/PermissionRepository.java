package com.microservice.user.persitence.repository;

import com.microservice.user.persitence.model.entities.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    // Buscar permisos por nombre
    List<PermissionEntity> findByNameIn(List<String> names);
}
