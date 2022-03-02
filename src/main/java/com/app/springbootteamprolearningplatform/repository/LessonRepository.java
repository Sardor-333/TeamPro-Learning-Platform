package com.app.springbootteamprolearningplatform.repository;

import com.app.springbootteamprolearningplatform.model.Lesson;
import com.app.springbootteamprolearningplatform.model.LessonComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    @Query(value =
            "from lesson_comments lc " +
                    "join lc.lesson l " +
                    "where l.id = ?1")
    List<LessonComment> findLessonComments(UUID lessonId);

    List<Lesson> findAllByModuleId(UUID moduleId);
}
