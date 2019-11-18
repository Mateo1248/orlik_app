package com.orlikteam.orlikbackend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "UPDATE users SET user_password=?2 WHERE user_login=?1", nativeQuery = true)
    void updateUserPassword(String userLogin, String newPassword);

}


