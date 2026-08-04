package com.movie.demo.endpoint.rest.model;

import com.movie.demo.domain.Genre;
import java.time.Duration;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieDto {
    private UUID id;
    private String title;
    private Genre genre;
    private String description;
    private Duration duration;
}