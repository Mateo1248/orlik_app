package com.orlikteam.orlikbackend.user

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class UserServiceSpec extends Specification {

    User user
    Optional<User> optionalUser
    private UserService userService
    private UserRepository userRepository

    def setup() {
        userRepository = Mock(UserRepository)
        userService = new UserService(userRepository)
        user = User.builder().userLogin("login").userPassword("pswd").build()
    }

    def "should add user to db"() {
        given:
            userRepository.save(user) >> user
        when:
            def resultOfAdd = userService.addUser(user)
        then:
            resultOfAdd.userLogin == "login"
            resultOfAdd.userPassword == "pswd"
    }

    def "should get one user"(){
        given:
            userRepository.findById(user.userLogin) >> Optional.of(user)
        when:
            def resultOfGet = userService.getUser(user.userLogin)
        then:
            resultOfGet.userPassword=="pswd"
    }


    def "should not get one user - throw exception"() {
        given:
            userRepository.findById(user.userLogin) >> Optional.of(user)
        when:
            def resultOfGet = userService.getUser("xxx")
        then:
            def e = thrown(NullPointerException)
    }

    def "should remove user"() {
        given:
            userRepository.findById(user.userLogin) >> Optional.of(user)
        when:
            userService.removeUser(user.userLogin)
        then:
            1*userRepository.deleteById(user.userLogin)
    }

    def "should not remove one user - throw exception"() {
        given:
            userRepository.findById(user.userLogin) >> Optional.of(user)
        when:
            userService.removeUser("xxx")
        then:
            def e = thrown(NullPointerException)
    }

}