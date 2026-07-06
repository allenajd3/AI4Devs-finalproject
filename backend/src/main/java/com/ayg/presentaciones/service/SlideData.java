package com.ayg.presentaciones.service;

/**
 * Representa los datos de una diapositiva individual generados por el LLM.
 * 
 * @param slideNumber Número de orden de la diapositiva (1-based)
 * @param title Título corto de la diapositiva
 * @param content Contenido textual en español (bullets o párrafo)
 * @param description Descripción completa en inglés para generación de imagen
 */
public record SlideData(
    int slideNumber,
    String title,
    String content,
    String description
) {
}
