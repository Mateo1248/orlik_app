package com.orlikteam.orlikbackend.user

import org.hibernate.Hibernate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class UserResourceSpec extends  Specification {

    User user
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
            //Hibernate.initialize(tmp)
            def resultOfGet = userResource.getUser("login")
        then:
            resultOfGet.userPassword=="pswd"
    }
}
