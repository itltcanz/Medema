package dev.itltcanz.medema.services;

import com.google.common.eventbus.EventBus;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.application.Platform;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class EventBufferService {

  private final EventBus eventBus;
  private final Queue<Object> buffer = new ConcurrentLinkedQueue<>();
  private volatile boolean uiReady = false;

  public void post(Object event) {
    if (!uiReady) {
      buffer.add(event);
      return;
    }
    eventBus.post(event);
  }

  public void active() {
    this.uiReady = true;
    Platform.runLater(this::flushBuffer);
  }

  private void flushBuffer() {
    Object event;
    while ((event = buffer.poll()) != null) {
      eventBus.post(event);
    }
  }
}