package dev.itltcanz.medema.event.status;

import dev.itltcanz.medema.model.enums.DetectorStatus;
import dev.itltcanz.medema.model.entity.Detector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.event.Level;

@Getter
@RequiredArgsConstructor
public class UpdateDetectorStatusEvent {
  private final Detector detector;
  private final Level level;
  private final DetectorStatus status;
  private final String message;
  private final Exception exception;
}
