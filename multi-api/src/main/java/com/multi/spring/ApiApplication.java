package com.multi.spring;

import com.multi.process.rest.annotation.EnableStub;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
/*@EnableAccessCheck*/
@EnableStub
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
