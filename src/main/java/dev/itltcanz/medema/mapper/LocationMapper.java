package dev.itltcanz.medema.mapper;

import dev.itltcanz.medema.model.dto.location.CreateLocationDto;
import dev.itltcanz.medema.model.dto.location.UpdateLocationDto;
import dev.itltcanz.medema.model.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface LocationMapper {

  @Mapping(target = "id", ignore = true)
  Location toEntity(CreateLocationDto dto);

  Location toEntity(UpdateLocationDto dto);
}