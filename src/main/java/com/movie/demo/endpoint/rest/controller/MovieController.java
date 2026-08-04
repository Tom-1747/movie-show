package com.movie.demo.endpoint.rest.controller;

import com.movie.demo.endpoint.rest.model.MovieDto;
import com.movie.demo.endpoint.rest.model.MovieInputDto;
import com.movie.demo.service.MovieService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MovieController {

  private final MovieService movieService;

  @GetMapping("/movies")
  public ResponseEntity<List<MovieDto>> getMovies() {
    var result = movieService.getMovies();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/movies/{movieId}")
  public ResponseEntity<MovieDto> getMovie(@PathVariable UUID movieId) {
    var result = movieService.getMovie(movieId);
    return ResponseEntity.ok(result);
  }

  @PutMapping("/movies")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<MovieDto> createOrUpdateMovie(
      @RequestParam(required = false) UUID movieId, @RequestBody MovieInputDto input) {
    var result = movieService.createOrUpdateMovie(movieId, input);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/movies/{movieId}")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<Void> deleteMovie(@PathVariable UUID movieId) {
    movieService.deleteMovie(movieId);
    return ResponseEntity.noContent().build();
  }
}
