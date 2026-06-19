package dev.itltcanz.medema.model.dto.detector;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class DeleteDetectorDto {
  @NotBlank(message = "id не может быть пустым")
  @Pattern(
      regexp = "^\\d{8}$",
      message = "id должен состоять ровно из 8 цифр"
  )
  private final String id;
}
