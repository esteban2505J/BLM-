package com.microservice.user.persitence.repository;

import com.microservice.user.persitence.model.entities.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
}
