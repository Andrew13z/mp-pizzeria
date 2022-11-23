package com.example.clientapp.exception.handler;

import com.example.clientapp.dto.ErrorDto;
import com.example.clientapp.exception.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.function.Function;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ControllerAdvice
public class ApplicationExceptionHandler {

  private final Map<Class<?>, Function<Exception, ResponseEntity<ErrorDto>>> hadlerMap =
    Map.of(EntityNotFoundException.class, this::handleEntityNotFoundException);

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleException(Exception exception) {
    return hadlerMap.getOrDefault(exception.getClass(), this::handleDefaultException)
      .apply(exception);
  }

  public ResponseEntity<ErrorDto> handleEntityNotFoundException(Exception exception) {
    return ResponseEntity.status(NOT_FOUND)
      .contentType(APPLICATION_JSON)
      .body(new ErrorDto(NOT_FOUND.getReasonPhrase(), exception.getMessage()));
  }

  private ResponseEntity<ErrorDto> handleDefaultException(Exception exception) {
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
      .contentType(APPLICATION_JSON)
      .body(new ErrorDto(INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage()));
  }
}
