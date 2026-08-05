package com.movie.demo.endpoint.rest.model;

import java.util.UUID;

public record SeatAvailabilityResponse(UUID id, String number, UUID roomId, boolean available) {}
