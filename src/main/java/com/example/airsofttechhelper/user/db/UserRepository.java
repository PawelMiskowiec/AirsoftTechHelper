package com.example.airsofttechhelper.user.db;

import com.example.airsofttechhelper.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
