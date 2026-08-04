package com.movie.demo.service;

import java.util.UUID;

public class ProjectionNotFoundException extends RuntimeException {
  public ProjectionNotFoundException(UUID id) {
    super("Projection not found: " + id);
  }
}
