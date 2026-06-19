package dev.itltcanz.medema.services;

import dev.itltcanz.medema.model.enums.DetectorStatus;
import dev.itltcanz.medema.event.status.DeleteDetectorStatusEvent;
import dev.itltcanz.medema.event.status.UpdateDetectorStatusEvent;
import dev.itltcanz.medema.factory.TrackerFactory;
import dev.itltcanz.medema.mapper.DetectorMapper;
import dev.itltcanz.medema.model.dto.detector.CreateDetectorDto;
import dev.itltcanz.medema.model.dto.detector.DeleteDetectorDto;
import dev.itltcanz.medema.model.dto.detector.UpdateDetectorDto;
import dev.itltcanz.medema.model.entity.Detector;
import dev.itltcanz.medema.repositories.DetectorRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.event.Level;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class DetectorService {

  private final DetectorMapper detectorMapper;
  private final DetectorRepository detectorRepository;
  private final ValidationService validationService;
  private final EventBufferService eventBus;
  private final TrackerFactory trackerFactory;

  public Detector create(CreateDetectorDto dto) {
    validationService.validate(dto);
    Detector detector = detectorMapper.toEntity(dto);
    if (detectorRepository.exists(detector.getId())) {
      throw new EntityExistsException("Детектор с таким id уже существует");
    }
    detectorRepository.create(detector);
    trackerFactory.create(detector);
    return detector;
  }

  public List<Detector> findAll() {
    return detectorRepository.findAll();
  }

  public Detector update(UpdateDetectorDto dto) {
    validationService.validate(dto);
    Detector detector = detectorMapper.toEntity(dto);
    if (!detectorRepository.exists(detector.getId())) {
      throw new EntityNotFoundException("Детектора с таким id не существует");
    }
    detector = detectorRepository.update(detector);
    trackerFactory.update(detector);
    eventBus.post(new UpdateDetectorStatusEvent(detector, Level.INFO, DetectorStatus.UPDATE,
        "Детектор обновлен", null));
    return detector;
  }

  public void delete(DeleteDetectorDto dto) {
    validationService.validate(dto);
    if (!detectorRepository.exists(dto.getId())) {
      throw new EntityNotFoundException("Детектора с таким id не существует");
    }
    detectorRepository.delete(dto.getId());
    trackerFactory.delete(dto.getId());
    eventBus.post(new DeleteDetectorStatusEvent(dto.getId()));
  }
}
