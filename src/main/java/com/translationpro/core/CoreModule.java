package com.translationpro.core;

import com.google.inject.AbstractModule;
import com.translationpro.core.tm.TranslationMemoryService;
import com.translationpro.core.tm.H2TranslationMemoryService;
import com.translationpro.core.project.ProjectService;
import com.translationpro.core.project.ProjectServiceImpl;
import com.translationpro.core.document.DocumentService;
import com.translationpro.core.document.DocumentServiceImpl;

/**
 * Guice module for dependency injection configuration.
 */
public class CoreModule extends AbstractModule {

    @Override
    protected void configure() {
        // Core services
        bind(TranslationMemoryService.class).to(H2TranslationMemoryService.class).asEagerSingleton();
        bind(ProjectService.class).to(ProjectServiceImpl.class).asEagerSingleton();
        bind(DocumentService.class).to(DocumentServiceImpl.class).asEagerSingleton();
    }
}
