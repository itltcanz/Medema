package dev.itltcanz.medema.config;

import dev.itltcanz.medema.exception.GlobalExceptionHandler;
import jakarta.inject.Inject;

public class GlobalExceptionHandlerConfig {
  @Inject
  public GlobalExceptionHandlerConfig(GlobalExceptionHandler globalExceptionHandler) {
    Thread.setDefaultUncaughtExceptionHandler(
        (t, e) -> globalExceptionHandler.handleException(e)
    );
  }

}
