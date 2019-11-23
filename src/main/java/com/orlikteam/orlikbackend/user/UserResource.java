package com.orlikteam.orlikbackend.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserResource {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserResource(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserLoginDto addUser(@RequestBody @Validated UserDto user) {
        user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
        return userService.addUser(user);
    }

    @DeleteMapping("/{userLogin}")
    public void removeUser(@PathVariable String userLogin) {
        userService.removeUser(userLogin);
    }

    @GetMapping("/{userLogin}")
    public String getUser(@PathVariable String userLogin) {
        return userService.getUser(userLogin);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody @Validated UserDto user) {
        user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
        userService.updateUser(user);
    }

}
