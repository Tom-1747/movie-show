package com.movie.demo.service;

import com.movie.demo.domain.Room;
import com.movie.demo.domain.Seat;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * Placeholder until the Rooms/Seats owner provides a real implementation. Delete this class when a
 * real {@link RoomService} bean is added. Projection unit tests mock {@link RoomService} and do not
 * rely on this bean.
 */
@Service
public class RoomServiceStub implements RoomService {
  @Override
  public Room getById(UUID roomId) {
    throw new UnsupportedOperationException("RoomService not implemented yet: " + roomId);
  }

  @Override
  public List<Seat> getSeatsByRoomId(UUID roomId) {
    throw new UnsupportedOperationException(
        "RoomService.getSeatsByRoomId not implemented yet: " + roomId);
  }
}
