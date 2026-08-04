package com.movie.demo.service;

import java.util.Set;
import java.util.UUID;

/** To be implemented by the Reservations owner; mocked in Projection tests. */
public interface ReservationService {
  Set<UUID> findReservedSeatIdsByProjectionId(UUID projectionId);
}
