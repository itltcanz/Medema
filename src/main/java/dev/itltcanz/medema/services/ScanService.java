package dev.itltcanz.medema.services;

import dev.itltcanz.medema.model.entity.Detector;
import dev.itltcanz.medema.model.entity.Scan;
import dev.itltcanz.medema.repositories.ScanRepository;
import dev.itltcanz.medema.util.SoundUtil;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ScanService {

  private final ScanRepository scanRepository;

  public void registerScan(Detector detector, byte metal) {
    LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    if (scanRepository.existsByDetectorAndTime(detector.getId(), time)) {
      throw new EntityExistsException("Скан с таким временем уже существует");
    }

    Scan scan = new Scan(detector, metal, time);
    scanRepository.create(scan);

    if (metal == 1) {
      new Thread(SoundUtil::play).start();
    }
  }

  public List<Scan> findScansForToday(Page page) {
    LocalDate today = LocalDate.now();
    LocalDateTime start = today.atStartOfDay();
    LocalDateTime end = today.plusDays(1).atStartOfDay();
    return scanRepository.findScansWithFilter(start, end, null, page);
  }

  public List<Scan> findScansWithFilters(LocalDateTime start, LocalDateTime end, String param,
      Page page) {
    return scanRepository.findScansWithFilter(start, end, param, page);
  }

}