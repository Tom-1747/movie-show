package com.movie.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.movie.demo.domain.Movie;
import com.movie.demo.domain.Projection;
import com.movie.demo.domain.Reservation;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ProjectionServiceTest {

  @Mock private ProjectionRepository projectionRepository;
  @Mock private MovieRepository movieRepository;
  @Mock private RoomRepository roomRepository;
  @Mock private SeatRepository seatRepository;
  @Mock private ReservationRepository reservationRepository;

  @InjectMocks private ProjectionService projectionService;

  private final UUID projectionId = UUID.fromString("11111111-1111-1111-1111-111111111111");
  private final UUID movieId = UUID.fromString("22222222-2222-2222-2222-222222222222");
  private final UUID roomId = UUID.fromString("33333333-3333-3333-3333-333333333333");
  private final UUID seatId = UUID.fromString("44444444-4444-4444-4444-444444444444");
  private final Instant datetime = Instant.parse("2026-08-10T18:30:00Z");

  @Test
  void list_mapsEntitiesToResponses() {
    when(projectionRepository.findAll()).thenReturn(List.of(sampleProjection()));

    List<ProjectionResponse> result = projectionService.list();

    assertEquals(1, result.size());
    assertEquals(projectionId, result.get(0).id());
    assertEquals(movieId, result.get(0).movieId());
    assertEquals(roomId, result.get(0).roomId());
  }

  @Test
  void getById_whenMissing_throwsNotFound() {
    when(projectionRepository.findById(projectionId)).thenReturn(Optional.empty());

    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> projectionService.getById(projectionId));

    assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
  }

  @Test
  void createOrUpdate_usesMovieAndRoomRepositories() {
    Movie movie = Movie.builder().id(movieId).build();
    Room room = Room.builder().id(roomId).build();
    when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
    when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
    when(projectionRepository.save(any(Projection.class)))
        .thenAnswer(
            invocation -> {
              Projection saved = invocation.getArgument(0);
              saved.setId(projectionId);
              return saved;
            });

    ProjectionRequest request =
        new ProjectionRequest(datetime, new BigDecimal("15.00"), movieId, roomId);

    ProjectionResponse response = projectionService.createOrUpdate(request);

    ArgumentCaptor<Projection> captor = ArgumentCaptor.forClass(Projection.class);
    verify(projectionRepository).save(captor.capture());
    assertEquals(movie, captor.getValue().getMovie());
    assertEquals(room, captor.getValue().getRoom());
    assertEquals(projectionId, response.id());
    assertEquals(new BigDecimal("15.00"), response.seatPrice());
  }

  @Test
  void delete_removesExistingProjection() {
    when(projectionRepository.findById(projectionId)).thenReturn(Optional.of(sampleProjection()));

    projectionService.delete(projectionId);

    verify(projectionRepository).delete(any(Projection.class));
  }

  @Test
  void getSeats_marksReservedSeatsUnavailable() {
    UUID freeSeatId = UUID.fromString("55555555-5555-5555-5555-555555555555");
    Seat reservedSeat = Seat.builder().id(seatId).number("A1").build();
    Seat freeSeat = Seat.builder().id(freeSeatId).number("A2").build();

    when(projectionRepository.findById(projectionId)).thenReturn(Optional.of(sampleProjection()));
    when(seatRepository.findByRoomId(roomId)).thenReturn(List.of(reservedSeat, freeSeat));
    when(reservationRepository.findByProjectionId(projectionId))
        .thenReturn(List.of(Reservation.builder().seats(List.of(reservedSeat)).build()));

    List<SeatAvailabilityResponse> seats = projectionService.getSeats(projectionId);

    assertEquals(2, seats.size());
    assertFalse(seats.get(0).available());
    assertTrue(seats.get(1).available());
    verify(reservationRepository).findByProjectionId(projectionId);
    verify(seatRepository).findByRoomId(roomId);
  }

  private Projection sampleProjection() {
    return Projection.builder()
        .id(projectionId)
        .datetime(datetime)
        .seatPrice(new BigDecimal("12.50"))
        .movie(Movie.builder().id(movieId).build())
        .room(Room.builder().id(roomId).build())
        .build();
  }
}
