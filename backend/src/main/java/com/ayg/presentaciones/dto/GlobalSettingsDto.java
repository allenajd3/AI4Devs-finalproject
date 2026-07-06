package com.ayg.presentaciones.dto;

public record GlobalSettingsDto(
    String systemPrompt,
    String contentOrientation,
    String visualStyle,
    Boolean darkMode
) {}
