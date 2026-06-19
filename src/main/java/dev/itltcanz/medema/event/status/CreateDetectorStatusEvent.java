package dev.itltcanz.medema.event.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateDetectorStatusEvent {
  private final String detectorId;
  private final String locationName;
}
