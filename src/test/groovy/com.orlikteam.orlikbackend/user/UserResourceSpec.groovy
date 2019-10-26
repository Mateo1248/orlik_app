package com.orlikteam.orlikbackend.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class UserResourceSpec extends Specification {

    @Autowired
    UserResource userResource

    def "should add user to db"() {
        given:
        def user = getUser("login1", "pswd1")

        when:
        def createdUser = userResource.addUser(user)

        then:
        with(createdUser) {
            userLogin == "login1"
            userPassword == "pswd1"
        }
    }

    def "should get user from db by userLogin"() {
        given:
        def user = getUser("login2", "pswd2")
        userResource.addUser(user)

        when:
        def takenUser = userResource.getUser("login2")

        then:
        takenUser.userPassword == "pswd2"
    }

    def "should throw exception due to attempt of getting non existing user"() {
        when:
        userResource.getUser("nonExistingLogin")

        then:
        thrown(UserNotFoundException)
    }

    def "should remove user from db"() {
        given:
        def user = getUser("login3", "pswd3")
        userResource.addUser(user)

        when:
        userResource.removeUser("login3")
        userResource.getUser("login3")

        then:
        thrown(UserNotFoundException)
    }

    def "should throw UserNotFoundException while removing non existing user"() {
        when:
        userResource.removeUser("nonExistingUser")

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
