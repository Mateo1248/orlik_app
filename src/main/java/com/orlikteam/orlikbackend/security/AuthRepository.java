package com.orlikteam.orlikbackend.security;

import com.orlikteam.orlikbackend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<User, String> {
    User findByUserLogin(String userLogin);
}
