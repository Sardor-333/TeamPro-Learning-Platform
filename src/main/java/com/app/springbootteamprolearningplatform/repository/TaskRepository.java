package com.app.springbootteamprolearningplatform.repository;

import com.app.springbootteamprolearningplatform.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
