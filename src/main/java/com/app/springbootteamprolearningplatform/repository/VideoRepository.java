package com.app.springbootteamprolearningplatform.repository;

import com.app.springbootteamprolearningplatform.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {
    List<Video> findAllByLessonId(UUID lessonId);

    Video findByFileName(String fileName);
}
