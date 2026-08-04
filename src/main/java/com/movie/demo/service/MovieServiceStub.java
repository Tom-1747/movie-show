package com.movie.demo.service;

import com.movie.demo.domain.Movie;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

/**
 * Placeholder until the Movies owner provides a real implementation. Projection unit tests mock
 * {@link MovieService} and do not rely on this bean.
 */
@Service
@ConditionalOnMissingBean(MovieService.class)
public class MovieServiceStub implements MovieService {
  @Override
  public Movie getById(UUID movieId) {
    throw new UnsupportedOperationException("MovieService not implemented yet: " + movieId);
  }
}
