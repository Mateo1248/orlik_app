package com.orlikteam.orlikbackend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrlikBackendApplicationTests {

    @Autowired
    ApplicationContext context

    @Test
    void contextLoads() {
        expect: context
    }

}
