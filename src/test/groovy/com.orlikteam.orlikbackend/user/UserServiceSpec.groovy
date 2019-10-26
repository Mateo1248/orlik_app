package com.orlikteam.orlikbackend.user

import spock.lang.Specification

class UserServiceSpec extends Specification {

    private UserService userService
    private UserRepository userRepository

    def setup() {
        userRepository = Mock(UserRepository)
        userService = new UserService(userRepository)
    }

    def "should add user to db"() {
        given:
        def user = getUser("login1", "pswd1")
        userRepository.save(user) >> user

        when:
        def createdUser = userService.addUser(user)

        then:
        with (createdUser) {
            userLogin == "login1"
            userPassword == "pswd1"
        }
    }

    def "should get one user"(){
        given:
        def user = getUser("login2", "pswd2")
        userRepository.findById(user.userLogin) >> Optional.of(user)

        when:
        def takenUser = userService.getUser(user.userLogin)

        then:
        takenUser.userPassword=="pswd2"
    }


    def "should throw exception due to attempt of getting non existing user"() {
        given:
        userRepository.findById("nonExistingUser") >> Optional.empty()

        when:
        userService.getUser("nonExistingUser")

        then:
        thrown(UserNotFoundException)
    }

    def "should remove user"() {
        given:
        def user = getUser("login3", "pswd3")
        userRepository.findById(user.userLogin) >> Optional.of(user)

        when:
        userService.removeUser(user.userLogin)

        then:
        1*userRepository.deleteById(user.userLogin)
    }

    def "should throw exception while removing non existing user"() {
        given:
        userRepository.findById("nonExistingUser") >> Optional.empty()

        when:
        userService.removeUser("nonExistingUser")

        then:
        thrown(UserNotFoundException)
    }

    private static User getUser(String login, String password) {
        return User
                .builder()
                .userLogin(login)
                .userPassword(password)
                .build()
    }

}