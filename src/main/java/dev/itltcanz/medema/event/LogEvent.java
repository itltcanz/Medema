package dev.itltcanz.medema.event;

import dev.itltcanz.medema.model.entity.Detector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.event.Level;

@Getter
@RequiredArgsConstructor
public class LogEvent {
  private final Detector detector;
  private final Level level;
  private final String log;
  private final String message;
}
