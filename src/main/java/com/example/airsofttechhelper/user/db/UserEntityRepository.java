package com.example.airsofttechhelper.user.db;

import com.example.airsofttechhelper.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    @Query(" SELECT u " +
            " FROM UserEntity u JOIN FETCH u.roles " +
            " WHERE u.username = :username " )
    Optional<UserEntity> findByUsername(String username);
}
