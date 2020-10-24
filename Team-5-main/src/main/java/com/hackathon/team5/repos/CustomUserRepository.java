package com.hackathon.team5.repos;

import com.hackathon.team5.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {

    CustomUser findByEmail(String email);

    boolean existsByEmail(String email);

    CustomUser findByUuid(String uuid);
}
