package com.orlikteam.orlikbackend.user;

import com.orlikteam.orlikbackend.reservation.Reservation;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_login")
    @Email @NotBlank
    private String userLogin;

    @Column(name = "user_password")
    @NotBlank
    private String userPassword;

    @Transient
    @OneToMany(mappedBy = "which_user")
    private List<Reservation> reservations;
}
