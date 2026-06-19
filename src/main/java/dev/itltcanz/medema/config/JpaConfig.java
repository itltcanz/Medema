package dev.itltcanz.medema.config;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Singleton
public class JpaConfig implements Provider<EntityManagerFactory> {

  @Override
  public EntityManagerFactory get() {
    return Persistence.createEntityManagerFactory("medema-unit");
  }

}
