package com.orlikteam.orlikbackend.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class UserResourceSpec extends  Specification {

    User user
    Optional<User> tmpTester
    @Autowired
    UserResource userResource

    def setup() {
        user = User.builder().userLogin("login").userPassword("pswd").build()
    }

    def "should add user to db"() {
        when:
            def resultOfAdd = userResource.addUser(user)
        then:
            resultOfAdd.userLogin == "login"
            resultOfAdd.userPassword == "pswd"
    }

    def "should get user from db by userLogin"() {
        when:
            def tmp = userResource.addUser(user)
            def resultOfGet = userResource.getUser("login")
        then:
            resultOfGet.userPassword=="pswd"
    }

    // user with xxx login is not in db so thrown exception
    def "should throw UserException exception"() {
        when:
            tmpTester = userResource.getUser("xxx")
        then:
            def e = thrown(UserNotFoundException)
    }

    //added user, removed and checked if he is db, thrown exception because he was removed
    def "should remove user from db"() {
        when:
            def tmp = userResource.addUser(user)
            userResource.removeUser("login")
            tmpTester = userResource.getUser("login")
        then:
            def e = thrown(UserNotFoundException)
    }

    //user with S login is not in db, so thrown exception
    def "should throw IllegalArgException"() {
        when:
            userResource.removeUser("S")
        then:
            def e = thrown(UserNotFoundException)
    }
}
