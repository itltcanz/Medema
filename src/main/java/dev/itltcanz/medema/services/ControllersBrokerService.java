package dev.itltcanz.medema.services;

import static dev.itltcanz.medema.util.LogFormatterUtil.formatMessage;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import dev.itltcanz.medema.controller.DebugTabController;
import dev.itltcanz.medema.controller.DetectorStatusController;
import dev.itltcanz.medema.controller.TodayTabController;
import dev.itltcanz.medema.event.LogEvent;
import dev.itltcanz.medema.event.RefreshTableEvent;
import dev.itltcanz.medema.event.status.CreateDetectorStatusEvent;
import dev.itltcanz.medema.event.status.DeleteDetectorStatusEvent;
import dev.itltcanz.medema.event.status.UpdateDetectorStatusEvent;
import dev.itltcanz.medema.model.enums.DetectorStatus;
import javafx.application.Platform;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@AllArgsConstructor(onConstructor_ = {@Inject})
public class ControllersBrokerService {

  private final EventBus eventBus;
  private final TodayTabController todayTabController;
  private final DetectorStatusController detectorStatusController;
  private final DebugTabController debugTabController;

  @Inject
  public void postConstruct() {
    eventBus.register(this);
  }

  @Subscribe
  public void refreshTableHandler(RefreshTableEvent event) {
    runOnFxThread(todayTabController::updateTable);
  }

  @Subscribe
  public void logHandler(LogEvent event) {
    runOnFxThread(() -> {
      String debugMessage = formatMessage(event);
      debugTabController.addToDebug(debugMessage);
    });
  }

  @Subscribe
  public void createDetectorStatusHandler(CreateDetectorStatusEvent event) {
    runOnFxThread(() ->
        detectorStatusController.addDetectorStatus(event.getDetectorId(), event.getLocationName())
    );
  }

  @Subscribe
  public void updateDetectorStatusHandler(UpdateDetectorStatusEvent event) {
    runOnFxThread(() -> {
      if (event.getStatus() == DetectorStatus.ONLINE) {
        String message = formatMessage(event);
        debugTabController.addToDebug(message);
        detectorStatusController.setOnlineStatus(event.getDetector().getId());
        return;
      }
      if (event.getStatus() == DetectorStatus.OFFLINE) {
        String message = formatMessage(event);
        debugTabController.addToDebug(message);
        detectorStatusController.setOfflineStatus(event.getDetector().getId());
        return;
      }
      if (event.getStatus() == DetectorStatus.UPDATE) {
        detectorStatusController.updateDetectorStatus(
            event.getDetector().getId(),
            event.getDetector().getLocationName()
        );
      }
    });
  }

  @Subscribe
  public void deleteDetectorStatusHandler(DeleteDetectorStatusEvent event) {
    runOnFxThread(() ->
        detectorStatusController.deleteDetectorStatus(
            event.getDetectorId()
        )
    );
  }


  private void runOnFxThread(Runnable action) {
    if (Platform.isFxApplicationThread()) {
      action.run();
    } else {
      Platform.runLater(action);
    }
  }
} 