package com.movie.demo.endpoint.rest.controller;

import com.movie.demo.endpoint.rest.model.ErrorResponse;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex) {
    HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
    String message = ex.getReason() == null ? status.getReasonPhrase() : ex.getReason();
    ErrorResponse body = new ErrorResponse(Instant.now(), status.value(), message);
    return ResponseEntity.status(status).body(body);
  }
}
