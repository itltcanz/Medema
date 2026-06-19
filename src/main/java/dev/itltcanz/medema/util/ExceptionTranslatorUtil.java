package dev.itltcanz.medema.util;

import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionTranslatorUtil {

  public static String translate(Throwable throwable) {
    Throwable root = ExceptionUtils.getRootCause(throwable);

    if (root instanceof SQLiteException sqliteEx) {
      return mapSqliteError(sqliteEx.getResultCode());
    }

    if (root instanceof ConstraintViolationException violationEx) {
      return "Ошибка валидации полей:\n" + violationEx.getMessage().replace(", ", ",\n");
    }

    return throwable.getLocalizedMessage();
  }

  private static String mapSqliteError(SQLiteErrorCode errorCode) {
    return switch (errorCode) {
      case SQLITE_CONSTRAINT_UNIQUE -> "Запись с такими данными уже существует.";
      case SQLITE_CONSTRAINT_FOREIGNKEY ->
          "Невозможно удалить запись, так как она связана с другими данными.";
      case SQLITE_CONSTRAINT_NOTNULL -> "Заполнены не все обязательные поля.";
      default -> "Ошибка базы данных: " + errorCode.name();
    };
  }
}