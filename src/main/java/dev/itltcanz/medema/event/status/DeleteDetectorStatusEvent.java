package dev.itltcanz.medema.event.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeleteDetectorStatusEvent {
  private final String detectorId;
}
