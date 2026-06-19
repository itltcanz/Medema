package dev.itltcanz.medema.config;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Singleton
public class ValidatorConfig implements Provider<Validator> {
  @Override
  public Validator get() {
    try (var factory = Validation.buildDefaultValidatorFactory()) {
      return factory.getValidator();
    }
  }
}
