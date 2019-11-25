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

    /**
     * method is used to send a POST request to database (to send object of user to service)
     * @param user is a object made from given by user login and password (which is encoding here)
     * @return login of already added user got from service
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserLoginDto addUser(@RequestBody @Validated UserDto user) {
        user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
        return userService.addUser(user);
    }

    /**
     * method is used to send a DELETE request to database (to send login to service)
     * @param userLogin is a login of user who we are eager to delete from app
     */
    @DeleteMapping("/{userLogin}")
    public void removeUser(@PathVariable String userLogin) {
        userService.removeUser(userLogin);
    }


    /**
     * method is used to send a GET request to database (to send login to service)
     * @param userLogin is a login of user who we are eager to get from app
     */
    @GetMapping("/{userLogin}")
    public String getUser(@PathVariable String userLogin) {
        return userService.getUser(userLogin);
    }


    /**
     * method is used to send a PATCH request to database (to send object of user to service)
     * @param user is a object of user made from currently logged user's login and new password (which is encoding here)
     * @return login of user who already updated his password
     */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody @Validated UserDto user) {
        user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
        userService.updateUser(user);
    }

}
