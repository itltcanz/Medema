package dev.itltcanz.medema.exception;

import static dev.itltcanz.medema.util.ExceptionTranslatorUtil.translate;

import dev.itltcanz.medema.services.NotificationService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class GlobalExceptionHandler {

  private final NotificationService notificationService;

  public void handleException(Throwable throwable) {
    if (throwable == null) {
      notificationService.showError("Ошибка", "Непредвиденная ошибка");
      return;
    }

    String message = translate(throwable);

    notificationService.showError("Ошибка", message);
  }
}
