package com.orlikteam.orlikbackend.user.unit

import com.orlikteam.orlikbackend.user.User
import com.orlikteam.orlikbackend.user.UserDto
import com.orlikteam.orlikbackend.user.UserRepository
import com.orlikteam.orlikbackend.user.UserService
import com.orlikteam.orlikbackend.user.exception.UserAlreadyExistsException
import com.orlikteam.orlikbackend.user.exception.UserNotFoundException
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
        def maybeUser = getUser("login1@gmail.com", "pswd1")
        userRepository.save(maybeUser) >> maybeUser
        userRepository.findById(maybeUser.userLogin) >> Optional.empty()

        def user = buildUserDto("login1@gmail.com", "pswd1")

        when:
        def createdUser = userService.addUser(user)

        then:
        with (createdUser) {
            userLogin
        }
    }

    def "should throw exception due to already existing user with this login in db"() {
        given:
        def user = getUser("login1@gmail.com", "pswd1")
        userRepository.save(user) >> user
        userRepository.findById(user.userLogin) >> Optional.of(user)
        def duplicatedUser = buildUserDto("login1@gmail.com", "pswd1")

        when:
        userService.addUser(duplicatedUser)

        then:
        thrown(UserAlreadyExistsException)
    }

    def "should get one user"() {
        given:
        def user = getUser("login2@gmail.com", "pswd2")
        userRepository.findById(user.userLogin) >> Optional.of(user)

        when:
        def takenUserLogin = userService.getUser(user.userLogin)

        then:
        takenUserLogin =="login2@gmail.com"
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
        1 * userRepository.deleteById(user.userLogin)
    }

    def "should throw exception while removing non existing user"() {
        given:
        userRepository.findById("nonExistingUser") >> Optional.empty()

        when:
        userService.removeUser("nonExistingUser")

        then:
        thrown(UserNotFoundException)
    }

    def "should properly update login of user"() {
        given:
        def user = getUser("login3@gmail.com", "pswd3")
        userRepository.findById(user.userLogin) >> Optional.of(user)

        when:
        userService.updateUser("login3@gmail.com", "pswd4")

        then:
        1 * userRepository.updateUserPassword("login3@gmail.com", "pswd4")
    }


    def "should throw exception while updating non existing user"() {
        given:
        userRepository.findById("nonExistingUser") >> Optional.empty()

        when:
        userService.updateUser("nonExistingUser", "passwordForNonExistingUser")

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

    private static UserDto buildUserDto(String login, String password) {
        return UserDto
                .builder()
                .userLogin(login)
                .userPassword(password)
                .build()
    }

}