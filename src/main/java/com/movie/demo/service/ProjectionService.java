package com.movie.demo.service;

import com.movie.demo.domain.Movie;
import com.movie.demo.domain.Projection;
import com.movie.demo.domain.Room;
import com.movie.demo.domain.Seat;
import com.movie.demo.endpoint.rest.model.ProjectionRequest;
import com.movie.demo.endpoint.rest.model.ProjectionResponse;
import com.movie.demo.endpoint.rest.model.SeatAvailabilityResponse;
import com.movie.demo.repository.ProjectionRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class ProjectionService {
  private final ProjectionRepository projectionRepository;
  private final MovieService movieService;
  private final RoomService roomService;
  private final ReservationService reservationService;

  public List<ProjectionResponse> list() {
    return projectionRepository.findAll().stream().map(this::toResponse).toList();
  }

  public ProjectionResponse getById(UUID projectionId) {
    return toResponse(findProjectionOrThrow(projectionId));
  }

  public ProjectionResponse createOrUpdate(ProjectionRequest request) {
    Movie movie = movieService.getById(request.movieId());
    Room room = roomService.getById(request.roomId());

    Projection projection =
        Projection.builder()
            .datetime(request.datetime())
            .seatPrice(request.seatPrice())
            .movie(movie)
            .room(room)
            .build();

    return toResponse(projectionRepository.save(projection));
  }

  public void delete(UUID projectionId) {
    Projection projection = findProjectionOrThrow(projectionId);
    projectionRepository.delete(projection);
  }

  public List<SeatAvailabilityResponse> getSeats(UUID projectionId) {
    Projection projection = findProjectionOrThrow(projectionId);
    UUID roomId = projection.getRoom().getId();
    List<Seat> seats = roomService.getSeatsByRoomId(roomId);
    Set<UUID> reservedSeatIds = reservationService.findReservedSeatIdsByProjectionId(projectionId);

    return seats.stream()
        .map(
            seat ->
                new SeatAvailabilityResponse(
                    seat.getId(),
                    seat.getNumber(),
                    roomId,
                    !reservedSeatIds.contains(seat.getId())))
        .toList();
  }

  private Projection findProjectionOrThrow(UUID projectionId) {
    return projectionRepository
        .findById(projectionId)
        .orElseThrow(
            () ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Projection not found: " + projectionId));
  }

  private ProjectionResponse toResponse(Projection projection) {
    return new ProjectionResponse(
        projection.getId(),
        projection.getDatetime(),
        projection.getSeatPrice(),
        projection.getMovie().getId(),
        projection.getRoom().getId());
  }
}
