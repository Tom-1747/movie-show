package com.movie.demo.service;

import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * Placeholder until the Reservations owner provides a real implementation. Delete this class when a
 * real {@link ReservationService} bean is added. Projection unit tests mock {@link
 * ReservationService} and do not rely on this bean.
 */
@Service
public class ReservationServiceStub implements ReservationService {
  @Override
  public Set<UUID> findReservedSeatIdsByProjectionId(UUID projectionId) {
    throw new UnsupportedOperationException(
        "ReservationService not implemented yet: " + projectionId);
  }
}
