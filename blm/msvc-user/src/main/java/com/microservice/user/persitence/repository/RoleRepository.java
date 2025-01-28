package com.microservice.user.persitence.repository;

import com.microservice.user.persitence.model.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
