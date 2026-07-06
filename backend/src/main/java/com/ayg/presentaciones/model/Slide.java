package com.ayg.presentaciones.model;

import jakarta.persistence.*;
import java.util.UUID;

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

    @Column(columnDefinition = "TEXT", nullable = false)
    private String imagePrompt;

    @Column
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlideStatus status;

    public Slide() {
    }

    public Slide(UUID projectId, Integer order, String imagePrompt) {
        this.projectId = projectId;
        this.order = order;
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
