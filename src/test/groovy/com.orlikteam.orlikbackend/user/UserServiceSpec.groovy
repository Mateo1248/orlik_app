package com.orlikteam.orlikbackend.user

import spock.lang.Specification
import spock.lang.Subject

class UserServiceSpec extends Specification {

    @Subject
    User user
    UserService userService
    UserRepository userRepository = Mock()

    def setup() {
        userService = new UserService();
        user = new User();
        user.userLogin= "login"
        user.userPassword = "pswd"
    }

    def "should add user to db"() {
        when:
            userService.addUser(user)
        then:
            1*userRepository.save(user)
    }

    def "should remove user from db"() {
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
    }
}
