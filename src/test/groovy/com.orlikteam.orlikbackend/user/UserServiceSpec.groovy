package com.orlikteam.orlikbackend.user

import spock.lang.Specification

class UserServiceSpec extends Specification {

    User user1
    User user2
    User user3
    private UserService userService
    private UserRepository userRepository

    def setup() {
        userRepository = Mock(UserRepository)
        userService = new UserService(userRepository)
        user1 = User.builder().userLogin("login1").userPassword("pswd1").build()
        user2 = User.builder().userLogin("login2").userPassword("pswd2").build()
        user3 = User.builder().userLogin("login3").userPassword("pswd3").build()
    }

    def "should add user to db"() {
        given:
            userRepository.save(user1) >> user1
        when:
            def createdUser = userService.addUser(user1)
        then:
        with (createdUser) {
            userLogin == "login1"
            userPassword == "pswd1"
        }
    }

    def "should get one user"(){
        given:
            userRepository.findById(user2.userLogin) >> Optional.of(user2)
        when:
            def takenUser = userService.getUser(user2.userLogin)
        then:
            takenUser.userPassword=="pswd2"
    }


    def "should throw exception due to attempt of getting non existing user"() {
        given:
            userRepository.findById("nonExistingUser") >> Optional.empty()
        when:
            def takenUser = userService.getUser("nonExistingUser")
        then:
            thrown(UserNotFoundException)
    }

    def "should remove user"() {
        given:
            userRepository.findById(user3.userLogin) >> Optional.of(user3)
        when:
            userService.removeUser(user3.userLogin)
        then:
            1*userRepository.deleteById(user3.userLogin)
    }

    def "should throw exception while removing non existing user"() {
        given:
            userRepository.findById("nonExistingUser") >> Optional.empty()
        when:
            userService.removeUser("nonExistingUser")
        then:
            thrown(UserNotFoundException)
    }

}