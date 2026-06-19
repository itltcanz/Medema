package dev.itltcanz.medema;

import atlantafx.base.theme.PrimerLight;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.itltcanz.medema.config.GuiceConfig;
import dev.itltcanz.medema.exception.GlobalExceptionHandler;
import dev.itltcanz.medema.factory.TrackerFactory;
import dev.itltcanz.medema.services.EventBufferService;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("java:S6813")
public class MedemaApplication extends Application {

  private Injector injector;
  @Inject
  private EventBufferService eventBus;
  @Inject
  private TrackerFactory detectorTrackerFactory;
  @Inject
  private GlobalExceptionHandler globalExceptionHandler;
  @Inject
  private EntityManagerFactory emf;

  @Override
  public void init() {
    // Инициализация DI-контейнера
    this.injector = Guice.createInjector(new GuiceConfig());
    injector.injectMembers(this);
  }

  @Override
  public void start(Stage stage) {
    try {
      // Инициализация UI
      Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
      fxmlLoader.setControllerFactory(injector::getInstance);
      Scene scene = new Scene(fxmlLoader.load());
      stage.setTitle("Medema");
      stage.getIcons().add(new Image("/image/metal-detector.png"));
      stage.setScene(scene);
      stage.setMinHeight(400);
      stage.setMinWidth(600);
      stage.show();
      eventBus.active();
    } catch (Exception e) {
      globalExceptionHandler.handleException(e);
    }
  }

  @Override
  public void stop() {
    detectorTrackerFactory.stopAll();
    emf.close();
    Platform.exit();
  }
}