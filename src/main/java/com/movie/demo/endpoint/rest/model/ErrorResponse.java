package com.movie.demo.endpoint.rest.model;

import java.time.Instant;

public record ErrorResponse(Instant timestamp, int status, String message) {}
