package com.orlikteam.orlikbackend.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class UserServiceSpec extends Specification {

    User user
    @Autowired
    UserService userService
    @Autowired
    UserRepository userRepository

    def setup() {
        user = new User();
        user.userLogin = "login"
        user.userPassword = "pswd"
        //user = User.builder().userLogin("login").userPassword("pswd").build()
    }

    def "should add user to db"() {
        when:
        def result = userService.addUser(user)
        then:
        result.userLogin == "login"
        result.userPassword == "pswd"
    }
}

    /*def "should remove user from db"() {
        when:
            userService.removeUser(user.userLogin)
        then:
            1*userRepository.deleteById(user.userLogin)
    }

    def "should get one user from db"() {
        when:
            userService.getUser(user.userLogin)
        then:
            def oneUser = userRepository.getOne(user.userLogin)
            oneUser.userLogin=="login"
            oneUser.userPassword=="pswd"
    }*/



/*
1) błedy w teście o co caman
2) czy test powinien pokazać jakiej obsługi jakich wyjątków brakuje ?



etc coś tam zmienić i tam dać etc javahome
mock() do testowania serwisu
do testowania resource - trzeba postawić kontekst
 */