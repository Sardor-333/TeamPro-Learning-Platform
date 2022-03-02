package com.app.springbootteamprolearningplatform.repository;

import com.app.springbootteamprolearningplatform.model.Submission;
import com.app.springbootteamprolearningplatform.model.Task;
import com.app.springbootteamprolearningplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    boolean existsByUserAndTask(User user, Task task);
}
