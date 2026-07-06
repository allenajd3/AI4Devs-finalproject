package com.ayg.presentaciones.repository;

import com.ayg.presentaciones.model.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SlideRepository extends JpaRepository<Slide, UUID> {
    List<Slide> findByProjectIdOrderByOrderAsc(UUID projectId);
}
