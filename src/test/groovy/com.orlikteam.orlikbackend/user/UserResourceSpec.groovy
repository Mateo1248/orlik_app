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
        def user = getUser("login1@test.com", "pswd1")

        when:
        def createdUser = userResource.addUser(user)

        then:
        with(createdUser) {
            userLogin == "login1@test.com"
            userPassword == "pswd1"
        }
    }

    def "should get user from db by userLogin"() {
        given:
        def user = getUser("login2@test.com", "pswd2")
        userResource.addUser(user)

        when:
        def takenUser = userResource.getUser("login2@test.com")

        then:
        takenUser.userPassword == "pswd2"
    }

    def "should throw exception due to attempt of getting non existing user"() {
        when:
        userResource.getUser("nonExistingLogin@test.com")

        then:
        thrown(UserNotFoundException)
    }

    def "should remove user from db"() {
        given:
        def user = getUser("login3@test.com", "pswd3")
        userResource.addUser(user)

        when:
        userResource.removeUser("login3@test.com")
        userResource.getUser("login3@test.com")

        then:
        thrown(UserNotFoundException)
    }

    def "should throw UserNotFoundException while removing non existing user"() {
        when:
        userResource.removeUser("nonExistingUser@test.com")

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
