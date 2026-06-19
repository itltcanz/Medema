package dev.itltcanz.medema.services;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ValidationService {

  private final Validator validator;

  /**
   * Универсальный метод валидации.
   *
   * @param dto Объект для проверки правил аннотаций Jakarta
   * @param <T> Тип передаваемого объекта
   * @throws ConstraintViolationException если обнаружены ошибки валидации
   */
  public <T> void validate(T dto) {
    if (dto == null) {
      return;
    }
    Set<ConstraintViolation<T>> violations = validator.validate(dto);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(
          violations
      );
    }
  }

}
