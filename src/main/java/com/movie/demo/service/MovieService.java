package com.movie.demo.service;

import com.movie.demo.domain.Genre;
import com.movie.demo.domain.Movie;
import com.movie.demo.endpoint.rest.model.MovieDto;
import com.movie.demo.endpoint.rest.model.MovieInputDto;
import com.movie.demo.repository.MovieRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class MovieService {

  private final MovieRepository movieRepository;

  public List<MovieDto> getMovies() {
    return movieRepository.findAll().stream().map(this::toDto).toList();
  }

  public MovieDto getMovie(UUID movieId) {
    return movieRepository
        .findById(movieId)
        .map(this::toDto)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  public MovieDto createOrUpdateMovie(UUID movieId, MovieInputDto input) {
    var entity =
        movieId != null ? movieRepository.findById(movieId).orElseGet(Movie::new) : new Movie();

    entity.setTitle(input.getTitle());
    if (input.getGenre() != null) {
      entity.setGenre(Genre.valueOf(input.getGenre()));
    }
    entity.setDescription(input.getDescription());
    entity.setDuration(input.getDuration());

    var saved = movieRepository.save(entity);

    return toDto(saved);
  }

  private MovieDto toDto(Movie entity) {
    return new MovieDto(
        entity.getId(),
        entity.getTitle(),
        entity.getGenre(),
        entity.getDescription(),
        entity.getDuration());
  }

  public void deleteMovie(UUID movieId) {
    var entity =
        movieRepository
            .findById(movieId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    entity.setDeletedAt(Instant.now());
    movieRepository.save(entity);
  }
}
