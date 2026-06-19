package dev.itltcanz.medema.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateFormatterUtil {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

  public static String format(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.format(formatter) : "н.д.";
  }
}