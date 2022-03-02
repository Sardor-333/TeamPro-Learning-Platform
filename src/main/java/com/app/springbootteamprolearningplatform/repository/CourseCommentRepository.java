package com.app.springbootteamprolearningplatform.repository;

import com.app.springbootteamprolearningplatform.model.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseCommentRepository extends JpaRepository<CourseComment, UUID> {
}
