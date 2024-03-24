package br.com.vcruz.MoonProject.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message);
  }
}
