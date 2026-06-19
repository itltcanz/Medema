package dev.itltcanz.medema.model.dto.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UpdateLocationDto {

  @NotBlank(message = "id не может быть пустым")
  @Pattern(regexp = "^\\d+$", message = "id должен состоять только из цифр")
  private final String id;

  @NotBlank(message = "name не может быть пустым")
  @Size(min = 3, max = 30, message = "name должен быть от 3 до 30 символов")
  private final String name;
}
