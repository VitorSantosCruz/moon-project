package br.com.vcruz.MoonProject.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TokenException extends RuntimeException {
  public TokenException(String message) {
    super(message);
  }
}
