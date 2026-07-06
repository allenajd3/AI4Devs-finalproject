package com.ayg.presentaciones.service;

import java.util.List;

/**
 * Representa el outline completo de una presentación generado por el LLM.
 * Incluye el título de la presentación y la lista de slides.
 */
public record SlideOutline(
    String title,
    List<SlideData> slides
) {
}
