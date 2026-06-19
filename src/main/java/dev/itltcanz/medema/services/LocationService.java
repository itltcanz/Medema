package dev.itltcanz.medema.services;

import dev.itltcanz.medema.mapper.LocationMapper;
import dev.itltcanz.medema.model.dto.location.CreateLocationDto;
import dev.itltcanz.medema.model.dto.location.DeleteLocationDto;
import dev.itltcanz.medema.model.dto.location.UpdateLocationDto;
import dev.itltcanz.medema.model.entity.Location;
import dev.itltcanz.medema.repositories.LocationRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class LocationService {

  private final LocationMapper locationMapper;
  private final LocationRepository locationRepository;
  private final ValidationService validationService;

  public Location create(CreateLocationDto dto) {
    validationService.validate(dto);
    Location location = locationMapper.toEntity(dto);
    locationRepository.create(location);
    return location;
  }

  public List<Location> findAll() {
    return locationRepository.findAll();
  }


  public Location update(UpdateLocationDto dto) {
    validationService.validate(dto);
    Location location = locationMapper.toEntity(dto);
    if (!locationRepository.exists(location.getId())) {
      throw new EntityNotFoundException("Детектора с таким id не существует");
    }
    return locationRepository.update(location);
  }

  public void delete(DeleteLocationDto dto) {
    validationService.validate(dto);
    Long id = Long.parseLong(dto.getId());
    if (!locationRepository.exists(id)) {
      throw new EntityNotFoundException("Детектора с таким id не существует");
    }
    locationRepository.delete(Long.parseLong(dto.getId()));
  }
}
