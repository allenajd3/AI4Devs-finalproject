package com.ayg.presentaciones.dto;

import com.ayg.presentaciones.model.SlideStatus;
import java.util.UUID;

public record SlideResponse(
    UUID id,
    Integer order,
    String imagePrompt,
    String imageUrl,
    SlideStatus status
) {}
