package com.movie.demo.endpoint.rest.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProjectionRequest(
    Instant datetime, BigDecimal seatPrice, UUID movieId, UUID roomId) {}
