package com.movie.demo.service;

import com.movie.demo.domain.Movie;
import com.movie.demo.domain.Projection;
import com.movie.demo.domain.Room;
import com.movie.demo.domain.Seat;
import com.movie.demo.endpoint.rest.controller.rest.dto.ProjectionInput;
import com.movie.demo.endpoint.rest.controller.rest.dto.SeatAvailability;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Mock implementation of the projection business layer.
 *
 * <p>For now, persistence and the Movie/Room dependencies are mocked in-memory: {@code movieId} and
 * {@code roomId} from {@link ProjectionInput} are accepted as plain UUIDs without a real lookup.
 * Replace this with a JPA repository + real Movie/Room services once those are implemented.
 */
@Slf4j
@Service
public class ProjectionService {

  private final Map<UUID, Projection> store = new ConcurrentHashMap<>();

  public List<Projection> getAll() {
    return new ArrayList<>(store.values());
  }

  public Projection getById(UUID id) {
    return Optional.ofNullable(store.get(id))
        .orElseThrow(() -> new ProjectionNotFoundException(id));
  }

  public Projection save(ProjectionInput input) {
    var id = input.getId() == null ? UUID.randomUUID() : input.getId();
    var projection =
        Projection.builder()
            .id(id)
            .datetime(input.getDatetime())
            .seatPrice(input.getSeatPrice())
            .movie(mockMovie(input.getMovieId()))
            .room(mockRoom(input.getRoomId()))
            .build();
    store.put(id, projection);
    return projection;
  }

  public void delete(UUID id) {
    if (store.remove(id) == null) {
      throw new ProjectionNotFoundException(id);
    }
  }

  public List<SeatAvailability> getSeatAvailabilities(UUID projectionId) {
    var projection = getById(projectionId);
    var seats = Optional.ofNullable(projection.getRoom()).map(Room::getSeats).orElse(List.of());
    var result = new ArrayList<SeatAvailability>();
    for (Seat seat : seats) {
      result.add(SeatAvailability.builder().seat(seat).available(true).build());
    }
    return result;
  }

  private Movie mockMovie(UUID movieId) {
    if (movieId == null) {
      return null;
    }
    log.debug(
        "Mocking Movie dependency for id={} (real Movie service not implemented yet)", movieId);
    var movie = new Movie();
    movie.setId(movieId);
    return movie;
  }

  private Room mockRoom(UUID roomId) {
    if (roomId == null) {
      return null;
    }
    log.debug("Mocking Room dependency for id={} (real Room service not implemented yet)", roomId);
    var room = new Room();
    room.setId(roomId);
    return room;
  }
}
