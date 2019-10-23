package com.orlikteam.orlikbackend.user

import org.hibernate.Hibernate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class UserServiceSpec extends Specification {

    User user1
    User user2
    User user3
    @Autowired
    UserService userService
    @Autowired
    UserRepository userRepository

    def setup() {
        user1 = User.builder().userLogin("login").userPassword("pswd").build()
        //user3 = User.builder().userLogin("testadmin").userPassword("testpassword").build
    }

    def "should add user to db"() {
        when:
            def resultOfAdd = userService.addUser(user1)
        then:
            resultOfAdd.userLogin == "login"
            resultOfAdd.userPassword == "pswd"
    }


}




/*
etc coś tam zmienić i tam dać etc javahome
mock() do testowania serwisu
do testowania resource - trzeba postawić kontekst
autowired tylko w testach kontekstowych
w nietestach przez konstuktor
 */





/*def "should remove user from db"() {
        when:
            userService.addUser(user3)
            userService.removeUser(user3.userLogin)
        then:
            1*userRepository.deleteById(user3.userLogin)
    }

    def "should get one user from db"() {
        when:
        //Hibernate initialize sth
            def result = userService.getUser(user3.userLogin)
        then:
            result.userLogin=="testadmin"
            result.userPassword=="testpassword"
    }

    def "should catch NOT_FOUND exception"() {
        when:
            def result = userService.getUser(user2.userLogin)
        then:
            thrown UserNotFoundException
    }*/