package com.microservice.user.persitence.repository;

import com.microservice.user.persitence.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
