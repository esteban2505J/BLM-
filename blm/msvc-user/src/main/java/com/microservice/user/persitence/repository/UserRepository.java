package com.microservice.user.persitence.repository;

import com.microservice.user.persitence.model.entities.UserEntity;
import com.microservice.user.persitence.model.enums.Status;
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


//    Update the user's password
    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.password = :password WHERE u.id = :userId")
    void updatePassword(@Param("userId") Long userId, @Param("password") String password);

//    Get all users for status
    @Query("SELECT u FROM UserEntity u WHERE u.status = :status")
    List<UserEntity> findByUserStatus(@Param("status") Status status);

//    Get all the users for role and that are active
    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r.name = :roleName AND u.status = 'ACTIVE'")
    List<UserEntity> findActiveUsersByRole(@Param("roleName") String roleName);

}
