package com.app.springbootteamprolearningplatform;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootTeamProLearningPlatformApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTeamProLearningPlatformApplication.class, args);
    }

    @Override
    public void run(String... args) {
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
