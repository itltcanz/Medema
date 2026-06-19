package dev.itltcanz.medema.mapper;

import dev.itltcanz.medema.model.dto.detector.CreateDetectorDto;
import dev.itltcanz.medema.model.dto.detector.UpdateDetectorDto;
import dev.itltcanz.medema.model.entity.Detector;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface DetectorMapper {
  Detector toEntity(CreateDetectorDto dto);

  Detector toEntity(UpdateDetectorDto dto);
}
