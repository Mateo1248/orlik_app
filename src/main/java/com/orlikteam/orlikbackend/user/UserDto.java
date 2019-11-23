package com.orlikteam.orlikbackend.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserDto {
    @Email @NotBlank
    private String userLogin;
    @NotBlank
    private String userPassword;
}
