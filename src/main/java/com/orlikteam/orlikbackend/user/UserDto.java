package com.orlikteam.orlikbackend.user;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserDto {
    private String userLogin;
    private String userPassword;
}
