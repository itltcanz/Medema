package dev.itltcanz.medema.config;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public class ExecutorServiceConfig implements Provider<ExecutorService> {

  @Override
  public ExecutorService get() {
    return Executors.newCachedThreadPool(runnable -> {
      Thread thread = new Thread(runnable);
      thread.setDaemon(true);
      return thread;
    });
  }
}
