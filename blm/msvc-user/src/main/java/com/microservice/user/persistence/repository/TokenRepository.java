package com.microservice.user.persistence.repository;

import com.microservice.user.persistence.model.vo.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM TokenEntity t WHERE t.user.id = :userId")
    void deleteAllTokensByUserId(@Param("userId") Long userId);
}
