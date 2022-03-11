package com.app.springbootteamprolearningplatform.repository;

import com.app.springbootteamprolearningplatform.model.CourseRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRateRepository extends JpaRepository<CourseRate, UUID> {

    CourseRate findByCourseIdAndUserId(UUID courseId, UUID userId);


}
