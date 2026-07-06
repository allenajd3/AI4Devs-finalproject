package com.ayg.presentaciones.controller;

import com.ayg.presentaciones.dto.GlobalSettingsDto;
import com.ayg.presentaciones.model.GlobalSettings;
import com.ayg.presentaciones.service.GlobalSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
public class GlobalSettingsController {

    private final GlobalSettingsService settingsService;

    public GlobalSettingsController(GlobalSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<GlobalSettingsDto> getSettings() {
        GlobalSettings settings = settingsService.getSettings();
        GlobalSettingsDto dto = new GlobalSettingsDto(
            settings.getSystemPrompt(),
            settings.getContentOrientation(),
            settings.getVisualStyle(),
            settings.isDarkMode()
        );
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<GlobalSettingsDto> updateSettings(@RequestBody GlobalSettingsDto dto) {
        GlobalSettings settings = new GlobalSettings();
        settings.setSystemPrompt(dto.systemPrompt());
        settings.setContentOrientation(dto.contentOrientation());
        settings.setVisualStyle(dto.visualStyle());
        settings.setDarkMode(dto.darkMode() != null ? dto.darkMode() : false);

        GlobalSettings updated = settingsService.updateSettings(settings);

        GlobalSettingsDto response = new GlobalSettingsDto(
            updated.getSystemPrompt(),
            updated.getContentOrientation(),
            updated.getVisualStyle(),
            updated.isDarkMode()
        );
        return ResponseEntity.ok(response);
    }
}
