package dev.itltcanz.medema.repositories;

import dev.itltcanz.medema.model.entity.Detector;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;

public class DetectorRepository extends BaseRepository<Detector, String> {

  @Inject
  public DetectorRepository(EntityManagerFactory emf) {
    super(Detector.class, emf);
  }
}
