package com.ayg.presentaciones.dto;

import com.ayg.presentaciones.model.ProjectStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectResponse(
    UUID id,
    String title,
    String content,
    ProjectStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
