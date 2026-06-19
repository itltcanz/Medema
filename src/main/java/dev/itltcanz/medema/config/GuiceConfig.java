package dev.itltcanz.medema.config;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import dev.itltcanz.medema.controller.ArchiveTabController;
import dev.itltcanz.medema.controller.DebugTabController;
import dev.itltcanz.medema.controller.DetectorStatusController;
import dev.itltcanz.medema.controller.DetectorTabController;
import dev.itltcanz.medema.controller.LocationTabController;
import dev.itltcanz.medema.controller.TodayTabController;
import dev.itltcanz.medema.exception.GlobalExceptionHandler;
import dev.itltcanz.medema.mapper.DetectorMapper;
import dev.itltcanz.medema.mapper.DetectorMapperImpl;
import dev.itltcanz.medema.mapper.LocationMapper;
import dev.itltcanz.medema.mapper.LocationMapperImpl;
import dev.itltcanz.medema.services.ConnectionService;
import dev.itltcanz.medema.services.ControllersBrokerService;
import dev.itltcanz.medema.services.DetectorService;
import dev.itltcanz.medema.services.EventBufferService;
import dev.itltcanz.medema.services.LocationService;
import dev.itltcanz.medema.services.NotificationService;
import dev.itltcanz.medema.services.PdfService;
import dev.itltcanz.medema.services.ScanService;
import dev.itltcanz.medema.services.UiStateService;
import dev.itltcanz.medema.services.ValidationService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.validation.Validator;
import java.util.concurrent.ExecutorService;

public class GuiceConfig extends AbstractModule {

  @Override
  protected void configure() {
    // Ранняя регистрация
    bind(LiquibaseConfig.class).asEagerSingleton();
    bind(ControllersBrokerService.class).asEagerSingleton();
    bind(GlobalExceptionHandlerConfig.class).asEagerSingleton();
    bind(TrackersConfig.class).asEagerSingleton();
    bind(UiStateService.class).asEagerSingleton();

    // Регистрация поставщиков
    bind(ExecutorService.class).toProvider(ExecutorServiceConfig.class).in(Scopes.SINGLETON);
    bind(EntityManagerFactory.class).toProvider(JpaConfig.class).in(Scopes.SINGLETON);
    bind(Validator.class).toProvider(ValidatorConfig.class).in(Scopes.SINGLETON);
    bind(EventBus.class).toProvider(EventBusConfig.class).in(Scopes.SINGLETON);

    // Регистрация контроллеров
    bind(TodayTabController.class).in(Scopes.SINGLETON);
    bind(ArchiveTabController.class).in(Scopes.SINGLETON);
    bind(DetectorTabController.class).in(Scopes.SINGLETON);
    bind(LocationTabController.class).in(Scopes.SINGLETON);
    bind(DebugTabController.class).in(Scopes.SINGLETON);
    bind(DetectorStatusController.class).in(Scopes.SINGLETON);

    // Регистрация сервисов
    bind(DetectorService.class).in(Scopes.SINGLETON);
    bind(LocationService.class).in(Scopes.SINGLETON);
    bind(NotificationService.class).in(Scopes.SINGLETON);
    bind(PdfService.class).in(Scopes.SINGLETON);
    bind(ScanService.class).in(Scopes.SINGLETON);
    bind(ValidationService.class).in(Scopes.SINGLETON);
    bind(EventBufferService.class).in(Scopes.SINGLETON);

    // Регистрация prototype
    bind(ConnectionService.class);

    // Регистрация мапперов
    bind(LocationMapper.class).to(LocationMapperImpl.class);
    bind(DetectorMapper.class).to(DetectorMapperImpl.class);

    // Регистрация остального
    bind(GlobalExceptionHandler.class).in(Scopes.SINGLETON);
  }
}