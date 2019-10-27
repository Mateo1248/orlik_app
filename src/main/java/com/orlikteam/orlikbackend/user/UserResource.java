package com.orlikteam.orlikbackend.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public User addUser(@RequestBody @Valid User user) {
        user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
        return userService.addUser(user);
    }

    @DeleteMapping("/{userLogin}")
    public void removeUser(@PathVariable String userLogin) {
        userService.removeUser(userLogin);
    }

    @GetMapping("/{userLogin}")
    public User getUser(@PathVariable String userLogin) {
        return userService.getUser(userLogin);
    }

}
