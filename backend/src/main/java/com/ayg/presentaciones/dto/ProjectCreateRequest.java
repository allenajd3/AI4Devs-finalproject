package com.ayg.presentaciones.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectCreateRequest(
    @NotBlank(message = "Title is required")
    String title,
    
    @NotBlank(message = "Content is required")
    String content
) {}
