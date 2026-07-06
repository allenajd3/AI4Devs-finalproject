package com.ayg.presentaciones.service;

import org.springframework.core.io.Resource;

public interface StorageService {
    String store(byte[] content, String filename);
    Resource load(String filename);
}
