package dev.itltcanz.medema.config;

import com.google.common.eventbus.EventBus;
import jakarta.inject.Provider;

public class EventBusConfig implements Provider<EventBus> {
  @Override
  public EventBus get() {
    return new EventBus();
  }
}
