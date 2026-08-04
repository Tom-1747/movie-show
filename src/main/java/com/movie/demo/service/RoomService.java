package com.movie.demo.service;

import com.movie.demo.domain.Room;
import com.movie.demo.domain.Seat;
import java.util.List;
import java.util.UUID;

/** To be implemented by the Rooms/Seats owner; mocked in Projection tests. */
public interface RoomService {
  Room getById(UUID roomId);

  List<Seat> getSeatsByRoomId(UUID roomId);
}
