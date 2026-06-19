package dev.itltcanz.medema.config;

import dev.itltcanz.medema.factory.TrackerFactory;
import dev.itltcanz.medema.model.entity.Detector;
import dev.itltcanz.medema.services.DetectorService;
import jakarta.inject.Inject;
import java.util.List;

public class TrackersConfig {

  @Inject
  public TrackersConfig(TrackerFactory detectorTrackerFactory, DetectorService detectorService) {
    List<Detector> detectors = detectorService.findAll();
    detectorTrackerFactory.initializeAll(detectors);
  }

}
