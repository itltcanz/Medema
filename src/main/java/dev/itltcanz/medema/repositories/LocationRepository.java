package dev.itltcanz.medema.repositories;

import dev.itltcanz.medema.model.entity.Location;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;

public class LocationRepository extends BaseRepository<Location, Long> {

  @Inject
  public LocationRepository(EntityManagerFactory emf) {
    super(Location.class, emf);
  }
}
