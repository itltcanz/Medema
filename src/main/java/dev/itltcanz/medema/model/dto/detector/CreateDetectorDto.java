package dev.itltcanz.medema.model.dto.detector;

import dev.itltcanz.medema.model.entity.Location;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CreateDetectorDto {

  @NotBlank(message = "id не может быть пустым")
  @Pattern(
      regexp = "^\\d{8}$",
      message = "id должен состоять ровно из 8 цифр"
  )
  private final String id;

  @NotBlank(message = "ip адрес не может быть пустым")
  @Pattern(
      regexp = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$",
      message = "Некорректный формат IPv4 адреса"
  )
  private final String ip;

  @NotBlank(message = "port не может быть пустым")
  @Pattern(
      regexp = "^([1-9]\\d{0,3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$",
      message = "port должен быть числом в диапазоне от 1 до 65535"
  )
  private final String port;

  @NotNull(message = "location не может быть пустым")
  private final Location location;
}
