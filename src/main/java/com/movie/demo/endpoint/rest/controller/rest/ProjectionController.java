package com.movie.demo.endpoint.rest.controller.rest;

import com.movie.demo.domain.Projection;
import com.movie.demo.endpoint.rest.controller.rest.dto.ProjectionInput;
import com.movie.demo.endpoint.rest.controller.rest.dto.SeatAvailability;
import com.movie.demo.service.ProjectionService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProjectionController {

  private final ProjectionService projectionService;

  @GetMapping("/projections")
  public ResponseEntity<List<Projection>> listProjections() {
    return ResponseEntity.ok(projectionService.getAll());
  }

  @GetMapping("/projections/{projectionId}")
  public ResponseEntity<Projection> getProjection(@PathVariable UUID projectionId) {
    return ResponseEntity.ok(projectionService.getById(projectionId));
  }

  @PutMapping("/projections")
  public ResponseEntity<Projection> createOrUpdateProjection(@RequestBody ProjectionInput input) {
    return ResponseEntity.ok(projectionService.save(input));
  }

  @DeleteMapping("/projections/{projectionId}")
  public ResponseEntity<Void> deleteProjection(@PathVariable UUID projectionId) {
    projectionService.delete(projectionId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/projections/{projectionId}/seats")
  public ResponseEntity<List<SeatAvailability>> getProjectionSeats(
      @PathVariable UUID projectionId) {
    return ResponseEntity.ok(projectionService.getSeatAvailabilities(projectionId));
  }
}
