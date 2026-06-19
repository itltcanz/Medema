package dev.itltcanz.medema.model.dto.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class DeleteLocationDto {

  @NotBlank(message = "id не может быть пустым")
  @Pattern(regexp = "^\\d+$", message = "id должен состоять только из цифр")
  private final String id;

}
