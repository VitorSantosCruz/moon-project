package br.com.vcruz.MoonProject.exception;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ValidationException extends RuntimeException {
  private Map<String, String> errors;

  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(String message, Map<String, String> errors) {
    super(message);
    this.errors = errors;
  }
}
