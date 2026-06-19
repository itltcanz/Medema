package dev.itltcanz.medema.services;

import dev.itltcanz.medema.constant.TimerConstant;
import dev.itltcanz.medema.model.enums.DetectorStatus;
import dev.itltcanz.medema.event.LogEvent;
import dev.itltcanz.medema.event.RefreshTableEvent;
import dev.itltcanz.medema.event.status.UpdateDetectorStatusEvent;
import dev.itltcanz.medema.model.entity.Detector;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

@Slf4j
@Getter
@SuppressWarnings("java:S135")
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class TrackerService implements Runnable {

  private final Detector detector;
  private final ConnectionService connectionService;
  private final EventBufferService eventBus;
  private final ScanService scanService;
  private final MessageProcessorService messageProcessorService;
  private final AtomicBoolean threadStopFlag = new AtomicBoolean(false);
  private boolean isFirsMessage;

  @Override
  public void run() {
    while (!threadStopFlag.get()) {
      try {
        connectionService.connect(detector);
        isFirsMessage = true;
        updateStatus(Level.INFO, DetectorStatus.ONLINE, "Подключен", null);
      } catch (IOException e) {
        updateStatus(Level.ERROR, DetectorStatus.OFFLINE, "Ошибка подключения", e);
        sleep();
        continue;
      }
      while (!threadStopFlag.get()) {
        try {
          String message = connectionService.readMessage();
          addToDebug(Level.INFO, "Принято сообщение:", message);
          if (isFirsMessage) {
            isFirsMessage = false;
            continue;
          }
          messageProcessorService.process(detector, message);
          eventBus.post(new RefreshTableEvent());
        } catch (EntityExistsException e) {
          addToDebug(Level.ERROR, "Ошибка обработки сообщения:", e.getLocalizedMessage());
        } catch (Exception e) {
          updateStatus(Level.ERROR, DetectorStatus.OFFLINE, "Ошибка обработки сообщения", e);
          break;
        }
      }
    }
  }

  private void updateStatus(Level level, DetectorStatus status, String message, Exception e) {
    eventBus.post(new UpdateDetectorStatusEvent(
        detector,
        level,
        status,
        message,
        e
    ));
  }

  private void addToDebug(Level level, String log,String message) {
    eventBus.post(new LogEvent(detector, level, log, message));
  }

  private void sleep() {
    try {
      Thread.sleep(TimerConstant.RECONNECT_TIMEOUT);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void stop() throws IOException {
    threadStopFlag.set(true);
    connectionService.close();
  }
}