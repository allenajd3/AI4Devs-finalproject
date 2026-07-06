package com.ayg.presentaciones.service;

import com.ayg.presentaciones.model.GlobalSettings;
import com.ayg.presentaciones.repository.GlobalSettingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GlobalSettingsService {

    private static final String DEFAULT_SYSTEM_PROMPT = 
        "Eres un asistente experto en crear presentaciones ejecutivas profesionales. " +
        "Analiza el contenido proporcionado y genera una estructura clara, concisa y de alto impacto " +
        "adecuada para audiencias de nivel directivo.";

    private static final String DEFAULT_CONTENT_ORIENTATION = 
        "Enfoque profesional y ejecutivo, priorizando claridad, impacto y toma de decisiones.";

    private static final String DEFAULT_VISUAL_STYLE = 
        "Estilo corporativo, limpio, minimalista, colores sobrios y profesionales, " +
        "alta legibilidad, diseño moderno.";

    private final GlobalSettingsRepository repository;

    public GlobalSettingsService(GlobalSettingsRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public GlobalSettings getSettings() {
        return repository.findById(1L).orElseGet(this::createDefaultSettings);
    }

    @Transactional
    public GlobalSettings updateSettings(GlobalSettings settings) {
        settings.setId(1L); // Ensure single row
        return repository.save(settings);
    }

    private GlobalSettings createDefaultSettings() {
        GlobalSettings settings = new GlobalSettings();
        settings.setId(1L);
        settings.setSystemPrompt(DEFAULT_SYSTEM_PROMPT);
        settings.setContentOrientation(DEFAULT_CONTENT_ORIENTATION);
        settings.setVisualStyle(DEFAULT_VISUAL_STYLE);
        return repository.save(settings);
    }
}
