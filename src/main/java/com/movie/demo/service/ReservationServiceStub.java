package com.movie.demo.service;

import java.util.Set;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

/**
 * Placeholder until the Reservations owner provides a real implementation. Projection unit tests
 * mock {@link ReservationService} and do not rely on this bean.
 */
@Service
@ConditionalOnMissingBean(ReservationService.class)
public class ReservationServiceStub implements ReservationService {
  @Override
  public Set<UUID> findReservedSeatIdsByProjectionId(UUID projectionId) {
    throw new UnsupportedOperationException(
        "ReservationService not implemented yet: " + projectionId);
  }
}
