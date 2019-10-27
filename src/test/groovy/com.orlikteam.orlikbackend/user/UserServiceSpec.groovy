package com.orlikteam.orlikbackend.user

import spock.lang.Specification

class UserServiceSpec extends Specification {

    private UserService userService
    private UserRepository userRepository

    def setup() {
        userRepository = Mock(UserRepository)
        userService = new UserService(userRepository)
    }

    def "should throw exception due to bad mail format"() {
        given:
        def user = getUser("notproperRegexOfMail", "pswd")
        userRepository.save(user) >> user

        when:
        def createdUserWithBadMail = userService.addUser(user)

        then:
        thrown(UserBadMailException)
    }

    def "should add user to db"() {
        given:
        def user = getUser("login1@gmail.com", "pswd1")
        userRepository.save(user) >> user
        userRepository.findById(user.userLogin) >> Optional.empty()

        when:
        def createdUser = userService.addUser(user)

        then:
        with (createdUser) {
            userLogin == "login1@gmail.com"
            userPassword == "pswd1"
        }
    }

    def "should throw exception due to already existing user with this login in db"() {
        given:
        def user = getUser("login1@gmail.com", "pswd1")
        userRepository.save(user) >> user
        userRepository.findById(user.userLogin) >> Optional.of(user)

        when:
        def createdUser = userService.addUser(user)

        then:
        thrown(UserAlreadyInDBException)
    }

    def "should get one user"(){
        given:
        def user = getUser("login2@gmail.com", "pswd2")
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
        def user = getUser("login3@gmail.com", "pswd3")
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