package com.ayg.presentaciones.dto;

import java.util.UUID;

import com.ayg.presentaciones.model.SlideStatus;

public record SlideResponse(
    UUID id,
    Integer order,
    String title,
    String content,
    String imagePrompt,
    String imageUrl,
    SlideStatus status
) {}
