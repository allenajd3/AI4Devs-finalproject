package com.ayg.presentaciones.dto;

import com.ayg.presentaciones.model.ProjectStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectSummaryResponse(
    UUID id,
    String title,
    ProjectStatus status,
    LocalDateTime createdAt
) {}
