package com.movie.demo.service;

import com.movie.demo.domain.Movie;
import com.movie.demo.domain.Projection;
import com.movie.demo.domain.Room;
import com.movie.demo.domain.Seat;
import com.movie.demo.endpoint.rest.model.ProjectionRequest;
import com.movie.demo.endpoint.rest.model.ProjectionResponse;
import com.movie.demo.endpoint.rest.model.SeatAvailabilityResponse;
import com.movie.demo.repository.MovieRepository;
import com.movie.demo.repository.ProjectionRepository;
import com.movie.demo.repository.ReservationRepository;
import com.movie.demo.repository.RoomRepository;
import com.movie.demo.repository.SeatRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class ProjectionService {
  private final ProjectionRepository projectionRepository;
  private final MovieRepository movieRepository;
  private final RoomRepository roomRepository;
  private final SeatRepository seatRepository;
  private final ReservationRepository reservationRepository;

  public List<ProjectionResponse> list() {
    return projectionRepository.findAll().stream().map(this::toResponse).toList();
  }

  public ProjectionResponse getById(UUID projectionId) {
    return toResponse(findProjectionOrThrow(projectionId));
  }

  public ProjectionResponse createOrUpdate(ProjectionRequest request) {
    Movie movie = findMovieOrThrow(request.movieId());
    Room room = findRoomOrThrow(request.roomId());

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
    List<Seat> seats = seatRepository.findByRoomId(roomId);
    Set<UUID> reservedSeatIds = findReservedSeatIds(projectionId);

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

  private Set<UUID> findReservedSeatIds(UUID projectionId) {
    return reservationRepository.findByProjectionId(projectionId).stream()
        .flatMap(reservation -> reservation.getSeats().stream())
        .map(Seat::getId)
        .collect(Collectors.toSet());
  }

  private Movie findMovieOrThrow(UUID movieId) {
    return movieRepository
        .findById(movieId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found: " + movieId));
  }

  private Room findRoomOrThrow(UUID roomId) {
    return roomRepository
        .findById(roomId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found: " + roomId));
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
