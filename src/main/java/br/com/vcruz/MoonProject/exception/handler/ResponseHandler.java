package br.com.vcruz.MoonProject.exception.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.vcruz.MoonProject.exception.AuthenticationException;
import br.com.vcruz.MoonProject.exception.ExceptionResponseDto;
import br.com.vcruz.MoonProject.exception.NotFoundException;
import br.com.vcruz.MoonProject.exception.ValidationException;

@ControllerAdvice
public class ResponseHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler({ AuthenticationException.class })
  public ResponseEntity<ExceptionResponseDto> handleBadCredentialsException(Exception ex, WebRequest request) {
    var exceptionResponseDto = new ExceptionResponseDto(ex.getMessage(), UNAUTHORIZED);
    return new ResponseEntity<ExceptionResponseDto>(exceptionResponseDto, exceptionResponseDto.getStatus());
  }

  @ExceptionHandler({ NotFoundException.class })
  public ResponseEntity<ExceptionResponseDto> handleNotFoundException(Exception ex, WebRequest request) {
    var exceptionResponseDto = new ExceptionResponseDto(ex.getMessage(), NOT_FOUND);
    return new ResponseEntity<ExceptionResponseDto>(exceptionResponseDto, exceptionResponseDto.getStatus());
  }

  @ExceptionHandler({ ValidationException.class })
  public ResponseEntity<ExceptionResponseDto> handleValidationException(ValidationException ex, WebRequest request) {
    ExceptionResponseDto exceptionResponseDto;

    if (ex.getErrors() != null) {
      exceptionResponseDto = new ExceptionResponseDto(ex.getErrors(), BAD_REQUEST);
    } else {
      exceptionResponseDto = new ExceptionResponseDto(ex.getMessage(), BAD_REQUEST);
    }

    return new ResponseEntity<ExceptionResponseDto>(exceptionResponseDto, exceptionResponseDto.getStatus());
  }
}
