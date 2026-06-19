package dev.itltcanz.medema.services;

import jakarta.inject.Singleton;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class NotificationService {
  public void showError(String headerText, String contentText) {
    runOnFxThread(() -> {
      log.error(contentText);
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Ошибка");
      alert.setHeaderText(headerText);
      alert.setContentText(contentText);
      alert.showAndWait();
    });
  }

  public void showInfo(String headerText, String contentText) {
    runOnFxThread(() -> {
      log.info(contentText);
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Информация");
      alert.setHeaderText(headerText);
      alert.setContentText(contentText);
      alert.showAndWait();
    });
  }

  private void runOnFxThread(Runnable action) {
    if (Platform.isFxApplicationThread()) {
      action.run();
    } else {
      Platform.runLater(action);
    }
  }
}