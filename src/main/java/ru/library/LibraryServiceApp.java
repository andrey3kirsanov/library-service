package ru.library;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.library.config.ApplicationProperties;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
public class LibraryServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(LibraryServiceApp.class, args);
    }
}
