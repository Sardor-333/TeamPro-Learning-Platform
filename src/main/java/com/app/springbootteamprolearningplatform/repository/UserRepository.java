package com.app.springbootteamprolearningplatform.repository;

import com.app.springbootteamprolearningplatform.model.Role;
import com.app.springbootteamprolearningplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findAllByRoles(List<Role> roles);

    List<User> findAllByEmailContaining(String email); //       email LIKE '%email%'

    List<User> findAllByEmailLike(String email); //             email LIKE 'email'
}
