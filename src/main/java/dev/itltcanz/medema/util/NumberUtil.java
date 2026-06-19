package dev.itltcanz.medema.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberUtil {
  public static boolean isNumber(String str) {
    // проверяет целые числа с возможным минусом
    if (str == null || str.isEmpty()) return false;
    return str.matches("-?\\d+");
  }
}
