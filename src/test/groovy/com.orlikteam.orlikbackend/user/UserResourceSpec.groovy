package com.orlikteam.orlikbackend.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class UserResourceSpec extends  Specification {

    private User user1
    private User user2
    private User user3
    private Optional<User> nonExistingUser

    @Autowired
    UserResource userResource

    def setup() {
        user1 = User.builder().userLogin("login1").userPassword("pswd1").build()
        user2 = User.builder().userLogin("login2").userPassword("pswd2").build()
        user3 = User.builder().userLogin("login3").userPassword("pswd3").build()
    }

    def "should add user to db"() {
        when:
            def createdUser = userResource.addUser(user1)
        then:
            with (createdUser) {
                userLogin == "login1"
                userPassword == "pswd1"
            }
    }

    def "should get user from db by userLogin"() {
        when:
            def addedToBeGot = userResource.addUser(user2) //must add user to have anybody to get
            def takenUser = userResource.getUser("login2")
        then:
            takenUser.userPassword=="pswd2"
    }

    def "should throw exception due to attempt of getting non existing user"() {
        when:
            nonExistingUser = userResource.getUser("nonExistingLogin")
        then:
            thrown(UserNotFoundException)
    }

    def "should remove user from db"() {
        when:
            def tmp = userResource.addUser(user3)
            userResource.removeUser("login3")
            def takenUserButNotExisting = userResource.getUser("login3")
        then:
            thrown(UserNotFoundException)
    }

    def "should throw UserNotFoundException while removing non existing user"() {
        when:
            userResource.removeUser("nonExistingUser")
        then:
            thrown(UserNotFoundException)
    }

}
