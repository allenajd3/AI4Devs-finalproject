package com.ayg.presentaciones.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "slides")
public class Slide {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID projectId;

    @Column(name = "slide_order", nullable = false)
    private Integer order;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String imagePrompt;

    @Column
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlideStatus status;

    public Slide() {
    }

    // Constructor anterior - mantener para compatibilidad
    public Slide(UUID projectId, Integer order, String imagePrompt) {
        this.projectId = projectId;
        this.order = order;
        this.imagePrompt = imagePrompt;
        this.status = SlideStatus.PENDING;
    }

    // Nuevo constructor con title y content
    public Slide(UUID projectId, Integer order, String title, String content, String imagePrompt) {
        this.projectId = projectId;
        this.order = order;
        this.title = title;
        this.content = content;
        this.imagePrompt = imagePrompt;
        this.status = SlideStatus.PENDING;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePrompt() {
        return imagePrompt;
    }

    public void setImagePrompt(String imagePrompt) {
        this.imagePrompt = imagePrompt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SlideStatus getStatus() {
        return status;
    }

    public void setStatus(SlideStatus status) {
        this.status = status;
    }
}
