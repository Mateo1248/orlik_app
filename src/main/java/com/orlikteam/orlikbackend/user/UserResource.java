package com.orlikteam.orlikbackend.user;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(User user) { return userService.addUser(user); }

    @DeleteMapping("/{userLogin}")
    public void removeUser(@PathVariable String userLogin) {
        userService.removeUser(userLogin);
    }

    @GetMapping("/{userLogin}")
    public User getUser(@PathVariable String userLogin) {
        return userService.getUser(userLogin);
    }

}
