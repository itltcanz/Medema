package dev.itltcanz.medema.util;

import static dev.itltcanz.medema.util.DateFormatterUtil.format;

import dev.itltcanz.medema.event.LogEvent;
import dev.itltcanz.medema.event.status.UpdateDetectorStatusEvent;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogFormatterUtil {

  public static String formatMessage(LogEvent event) {
    return """
        [%s][%s][%s][%s][%s]
        %s%s
        
        """.formatted(
        format(LocalDateTime.now()),
        event.getLevel().toString(),
        event.getDetector().getId(),
        event.getDetector().getIp(),
        event.getDetector().getLocationName(),
        event.getLog(),
        reveal(event.getMessage())
    );
  }

  public static String formatMessage(UpdateDetectorStatusEvent event) {
    return """
        [%s][%s][%s][%s][%s]
        %s%s
        
        """.formatted(
        format(LocalDateTime.now()),
        event.getLevel().toString(),
        event.getDetector().getId(),
        event.getDetector().getIp(),
        event.getDetector().getLocationName(),
        event.getMessage(),
        reveal(event.getException())
    );
  }

  private static String reveal(Exception e) {
    if (e == null) {
      return "";
    }
    return "\n" + e.getLocalizedMessage();
  }

  private static String reveal(String s) {
    if (s == null || s.isBlank()) {
      return "";
    }
    return "\n" + s;
  }
}
