package br.com.vcruz.MoonProject.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ExceptionResponseDto {
  String error;

  @JsonInclude(Include.NON_NULL)
  Map<String, String> errors;

  HttpStatus status;

  public ExceptionResponseDto(String error, HttpStatus status) {
    this.error = error;
    this.status = status;
  }

  public ExceptionResponseDto(String error, Map<String, String> errors, HttpStatus status) {
    this.error = error;
    this.errors = errors;
    this.status = status;
  }
}
