package com.movie.demo.service;

import com.movie.demo.domain.Movie;
import java.util.UUID;

/** To be implemented by the Movies owner; mocked in Projection tests. */
public interface MovieService {
  Movie getById(UUID movieId);
}
