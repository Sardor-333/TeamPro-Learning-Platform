package com.app.springbootteamprolearningplatform.repository;

import com.app.springbootteamprolearningplatform.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<Module, UUID> {

    List<Module> findAllByCourseId(UUID course_id);

}
