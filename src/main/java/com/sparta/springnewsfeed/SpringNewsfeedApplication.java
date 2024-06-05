package com.sparta.springnewsfeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringNewsfeedApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringNewsfeedApplication.class, args);
    }
}