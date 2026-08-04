package com.movie.demo.endpoint.rest.model;

import java.time.Duration;
import lombok.Data;

@Data
public class MovieInputDto {
  private String title;
  private String genre;
  private String description;
  private Duration duration;
}
