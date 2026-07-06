package com.ayg.presentaciones.model;

import jakarta.persistence.*;

@Entity
@Table(name = "global_settings")
public class GlobalSettings {

    @Id
    private Long id = 1L; // Single row strategy

    @Column(columnDefinition = "TEXT")
    private String systemPrompt;

    @Column(columnDefinition = "TEXT")
    private String contentOrientation;

    @Column(columnDefinition = "TEXT")
    private String visualStyle;

    public GlobalSettings() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public String getContentOrientation() {
        return contentOrientation;
    }

    public void setContentOrientation(String contentOrientation) {
        this.contentOrientation = contentOrientation;
    }

    public String getVisualStyle() {
        return visualStyle;
    }

    public void setVisualStyle(String visualStyle) {
        this.visualStyle = visualStyle;
    }
}
