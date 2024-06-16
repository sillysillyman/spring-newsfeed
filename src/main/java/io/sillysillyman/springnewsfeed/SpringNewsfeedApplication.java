package io.sillysillyman.springnewsfeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringNewsfeedApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringNewsfeedApplication.class, args);
    }
}