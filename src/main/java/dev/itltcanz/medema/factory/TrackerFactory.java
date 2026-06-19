package dev.itltcanz.medema.factory;

import dev.itltcanz.medema.event.status.CreateDetectorStatusEvent;
import dev.itltcanz.medema.event.status.DeleteDetectorStatusEvent;
import dev.itltcanz.medema.model.entity.Detector;
import dev.itltcanz.medema.services.ConnectionService;
import dev.itltcanz.medema.services.EventBufferService;
import dev.itltcanz.medema.services.MessageProcessorService;
import dev.itltcanz.medema.services.ScanService;
import dev.itltcanz.medema.services.TrackerService;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class TrackerFactory {

  private final Map<String, TrackerService> trackers = new ConcurrentHashMap<>();
  private final Provider<ConnectionService> connectionServiceProvider;
  private final MessageProcessorService messageProcessorService;
  private final ExecutorService executorService;
  private final ScanService scanService;
  private final EventBufferService eventBus;

  public void initializeAll(Collection<Detector> detectors) {
    detectors.forEach(this::createAndStartInternal);
  }

  public void create(Detector detector) {
    createAndStartInternal(detector);
  }

  private void createAndStartInternal(Detector detector) {
    TrackerService trackerService = new TrackerService(
        detector,
        connectionServiceProvider.get(),
        eventBus,
        scanService,
        messageProcessorService
    );
    trackers.put(detector.getId(), trackerService);
    executorService.submit(trackerService);
    eventBus.post(new CreateDetectorStatusEvent(detector.getId(), detector.getLocationName()));
  }

  public void update(Detector detector) {
    TrackerService tracker = trackers.get(detector.getId());
    if (tracker != null) {
      delete(detector.getId());
      createAndStartInternal(detector);
    } else {
      createAndStartInternal(detector);
    }
  }

  public void delete(String detectorId) {
    TrackerService tracker = trackers.remove(detectorId);
    if (tracker != null) {
      try {
        tracker.stop();
      } catch (Exception e) {
        // Логирование ошибки остановки потока
      }
    }
    eventBus.post(new DeleteDetectorStatusEvent(detectorId));
  }

  public void stopAll() {
    trackers.values().forEach(tracker -> {
      try {
        tracker.stop();
      } catch (Exception ignored) {
        // Игнорируем, так как приложение всё равно закрывается
      }
    });
    executorService.shutdown();
  }
}