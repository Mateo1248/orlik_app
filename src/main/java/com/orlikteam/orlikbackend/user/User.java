package com.orlikteam.orlikbackend.user;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Column(name="user_login")
    @Id
        private String userLogin;
    @Column(name="user_password")
        private String userPassword;
}
