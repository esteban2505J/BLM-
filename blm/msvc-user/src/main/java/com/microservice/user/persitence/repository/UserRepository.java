package com.microservice.user.persitence.repository;

import com.microservice.user.persitence.model.entities.UserEntity;
import com.microservice.user.persitence.model.vo.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserName(String userName);
    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.tokens = :tokens WHERE u.id = :userId")
    void updateTokens(@Param("userId") Long userId, @Param("tokens") List<TokenEntity> tokens);
}
