package com.app.springbootteamprolearningplatform.repository;

import com.app.springbootteamprolearningplatform.model.LessonComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LessonCommentRepository extends JpaRepository<LessonComment, UUID> {

    void deleteByIdAndLessonId(UUID id, UUID lessonId);
}
