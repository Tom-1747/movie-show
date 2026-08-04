package com.movie.demo.endpoint.rest.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.demo.domain.Genre;
import com.movie.demo.domain.Movie;
import com.movie.demo.endpoint.rest.model.MovieInputDto;
import com.movie.demo.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MovieControllerIT {

  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:15-alpine")
          .withDatabaseName("test")
          .withUsername("test")
          .withPassword("test");

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
  }

  @Autowired private MockMvc mockMvc;
  @Autowired private MovieRepository movieRepository;
  @Autowired private ObjectMapper objectMapper;

  @Test
  public void getMovies_returnsSavedMovie() throws Exception {
    movieRepository.deleteAll();
    var m = new Movie();
    m.setTitle("The Test Movie");
    m.setGenre(Genre.DRAMA);
    m.setDescription("desc");
    m.setDuration(90);
    var saved = movieRepository.save(m);

    mockMvc
        .perform(get("/movies").with(jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("The Test Movie"));
  }

  @Test
  public void getMovieById_returnsMovie() throws Exception {
    movieRepository.deleteAll();
    var m = new Movie();
    m.setTitle("Single Movie");
    m.setGenre(Genre.COMEDY);
    m.setDescription("desc");
    m.setDuration(100);
    var saved = movieRepository.save(m);

    mockMvc
        .perform(get("/movies/" + saved.getId()).with(jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Single Movie"));
  }

  @Test
  public void createOrUpdateMovie_allowsManagerRole() throws Exception {
    movieRepository.deleteAll();
    var input = new MovieInputDto();
    input.setTitle("Created Movie");
    input.setGenre("ACTION");
    input.setDescription("desc");
    input.setDuration(120);

    mockMvc
        .perform(
            put("/movies")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_MANAGER")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Created Movie"));
  }

  @Test
  public void deleteMovie_allowsManagerRole() throws Exception {
    movieRepository.deleteAll();
    var m = new Movie();
    m.setTitle("ToDelete");
    m.setGenre(Genre.DRAMA);
    m.setDescription("desc");
    m.setDuration(80);
    var saved = movieRepository.save(m);

    mockMvc
        .perform(
            delete("/movies/" + saved.getId())
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_MANAGER"))))
        .andExpect(status().isNoContent());
  }
}
