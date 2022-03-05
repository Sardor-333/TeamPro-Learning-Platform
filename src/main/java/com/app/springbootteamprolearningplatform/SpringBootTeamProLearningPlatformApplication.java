package com.app.springbootteamprolearningplatform;

import com.app.springbootteamprolearningplatform.model.Role;
import com.app.springbootteamprolearningplatform.model.User;
import com.app.springbootteamprolearningplatform.repository.RoleRepository;
import com.app.springbootteamprolearningplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

//        // INITIALIZE ROLES
//        Role user = new Role("USER");
//        Role mentor = new Role("MENTOR");
//        Role admin = new Role("ADMIN");
//        Role superAdmin = new Role("SUPER_ADMIN");
//
//        roleRepository.save(user);
//        roleRepository.save(mentor);
//        roleRepository.save(admin);
//        roleRepository.save(superAdmin);
//
//        // INITIALIZE SUPER_ADMIN
//        User SUPER_ADMIN = new User("Sardor", "Shermatov", "sardor.faang.dev@gmail.com", "_ingMAFASE0308", "Software Engineer Sardor Shermatov", null);
//        SUPER_ADMIN.getRoles().add(superAdmin);

//        userRepository.save(SUPER_ADMIN);
    }
}
