package com.app.springbootteamprolearningplatform;

import com.app.springbootteamprolearningplatform.model.Category;
import com.app.springbootteamprolearningplatform.model.Course;
import com.app.springbootteamprolearningplatform.model.Role;
import com.app.springbootteamprolearningplatform.model.User;
import com.app.springbootteamprolearningplatform.repository.CategoryRepository;
import com.app.springbootteamprolearningplatform.repository.CourseRepository;
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
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;

    public SpringBootTeamProLearningPlatformApplication(UserRepository userRepository, RoleRepository roleRepository, CategoryRepository categoryRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void run(String... args) {

        // INITIALIZE ROLES
        Role user = new Role("USER");
        Role mentor = new Role("MENTOR");
        Role admin = new Role("ADMIN");
        Role superAdmin = new Role("SUPER_ADMIN");

        roleRepository.save(user);
        roleRepository.save(mentor);
        roleRepository.save(admin);
        roleRepository.save(superAdmin);

        // INITIALIZE SUPER_ADMIN
        User SUPER_ADMIN = new User("Sardor", "Shermatov", "sardor.faang.dev@gmail.com", "123456", "Software Engineer Sardor Shermatov", null, 0.0);
        SUPER_ADMIN.getRoles().add(superAdmin);

        userRepository.save(SUPER_ADMIN);

        Category c1 = new Category("Web Development");
        Category c2 = new Category("Back end");
        Category c3 = new Category("Front end");
        Category c4 = new Category("Design");
        Category c5 = new Category("Cyber Security");

        categoryRepository.save(c1);
        categoryRepository.save(c2);
        categoryRepository.save(c3);
        categoryRepository.save(c4);
        categoryRepository.save(c5);

        User mentor1 = new User("Alice", "Smith", "alice@gmail.com", "123456", "some bio", null, 100000.0);
        mentor1.getRoles().add(mentor);
        mentor1.getRoles().add(user);

        User mentor2 = new User("Mark", "Smith", "mark@gmail.com", "123456", "some bio", null, 150000.0);
        mentor2.getRoles().add(mentor);
        mentor2.getRoles().add(user);

        User mentor3 = new User("Tom", "Smith", "tom@gmail.com", "123456", "some bio", null, 150000.0);
        mentor3.getRoles().add(mentor);
        mentor3.getRoles().add(user);

        userRepository.save(mentor1);
        userRepository.save(mentor2);
        userRepository.save(mentor3);

        Course course = new Course("Java", "Some description", c1, 10000.0);
        Course course1 = new Course("JS", "Some description", c2, 20000.0);
        Course course2 = new Course("Python", "Some description", c3, 236500.0);
        Course course3 = new Course("C#", "Some description", c4, 45000.0);
        Course course4 = new Course("C++", "Some description", c5, 20000.0);
        Course course5 = new Course("Flutter", "Some description", c1, 41000.0);

        course.getAuthors().add(mentor1);
        course.getAuthors().add(mentor2);

        course1.getAuthors().add(mentor1);
        course1.getAuthors().add(mentor2);

        course2.getAuthors().add(mentor1);
        course2.getAuthors().add(mentor2);

        course3.getAuthors().add(mentor1);
        course3.getAuthors().add(mentor2);

        course4.getAuthors().add(mentor1);
        course4.getAuthors().add(mentor2);

        course5.getAuthors().add(mentor1);
        course5.getAuthors().add(mentor2);

        courseRepository.save(course);
        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);
        courseRepository.save(course4);
        courseRepository.save(course5);
    }
}
