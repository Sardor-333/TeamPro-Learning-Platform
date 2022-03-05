package com.app.springbootteamprolearningplatform;

import com.app.springbootteamprolearningplatform.repository.RoleRepository;
import com.app.springbootteamprolearningplatform.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootTeamProLearningPlatformApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(SpringBootTeamProLearningPlatformApplication.class, args);
    }

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public SpringBootTeamProLearningPlatformApplication(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

    }
}
