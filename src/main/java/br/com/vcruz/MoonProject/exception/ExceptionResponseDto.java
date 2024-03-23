package br.com.vcruz.MoonProject.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ExceptionResponseDto {
  String message;
  HttpStatus status;
}
