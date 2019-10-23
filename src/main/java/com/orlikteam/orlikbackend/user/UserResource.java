package com.orlikteam.orlikbackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;

@RestController
@RequestMapping("/users")

public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void addUser(User user) {
        userService.addUser(user);
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
