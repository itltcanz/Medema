package dev.itltcanz.medema.model.dto.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CreateLocationDto {
  @NotBlank(message = "name не может быть пустым")
  @Size(min = 3, max = 30, message = "name должен быть от 3 до 30 символов")
  private final String name;
}
