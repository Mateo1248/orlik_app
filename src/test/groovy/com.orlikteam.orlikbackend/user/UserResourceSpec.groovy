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

    def "should return not found exception"() {
        when:
            tmpTester = userResource.getUser("xxx")
        then:
            def e = thrown(UserNotFoundException)
    }
}
