package com.movie.demo.endpoint.rest.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProjectionResponse(
    UUID id, Instant datetime, BigDecimal seatPrice, UUID movieId, UUID roomId) {}
