package com.app.springbootteamprolearningplatform.repository;

import com.app.springbootteamprolearningplatform.model.CourseVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseVoteRepository extends JpaRepository<CourseVote, UUID> {
}
