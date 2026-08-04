package com.movie.demo.endpoint.rest.controller;

import com.movie.demo.endpoint.rest.model.ProjectionRequest;
import com.movie.demo.endpoint.rest.model.ProjectionResponse;
import com.movie.demo.endpoint.rest.model.SeatAvailabilityResponse;
import com.movie.demo.service.ProjectionService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projections")
@AllArgsConstructor
public class ProjectionController {
  private final ProjectionService projectionService;

  @GetMapping
  public List<ProjectionResponse> list() {
    return projectionService.list();
  }

  @PutMapping
  public ProjectionResponse createOrUpdate(@RequestBody ProjectionRequest request) {
    return projectionService.createOrUpdate(request);
  }

  @GetMapping("/{projectionId}")
  public ProjectionResponse getById(@PathVariable UUID projectionId) {
    return projectionService.getById(projectionId);
  }

  @DeleteMapping("/{projectionId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID projectionId) {
    projectionService.delete(projectionId);
  }

  @GetMapping("/{projectionId}/seats")
  public List<SeatAvailabilityResponse> getSeats(@PathVariable UUID projectionId) {
    return projectionService.getSeats(projectionId);
  }
}
