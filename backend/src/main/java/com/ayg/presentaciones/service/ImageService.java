package com.ayg.presentaciones.service;

public interface ImageService {
    /**
     * Generates a slide image based on slide content and design instructions.
     * @param title The slide title
     * @param content The slide content (bullet points or text)
     * @param description Visual design instructions
     * @param visualStyle Optional additional style guidance
     * @return Byte array of the generated image
     */
    byte[] generateImage(String title, String content, String description, String visualStyle);
}
