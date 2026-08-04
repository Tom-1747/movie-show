package com.movie.demo.service;

import com.movie.demo.domain.Room;
import com.movie.demo.domain.Seat;
import com.movie.demo.endpoint.rest.model.RoomDto;
import com.movie.demo.endpoint.rest.model.RoomInputDto;
import com.movie.demo.endpoint.rest.model.SeatDto;
import com.movie.demo.endpoint.rest.model.SeatInputDto;
import com.movie.demo.repository.RoomRepository;
import com.movie.demo.repository.SeatRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class RoomService {

  private final RoomRepository roomRepository;
  private final SeatRepository seatRepository;

  public List<RoomDto> getRooms() {
    return roomRepository.findAll().stream().map(this::toDto).toList();
  }

  public RoomDto getRoom(UUID roomId) {
    return roomRepository
        .findById(roomId)
        .map(this::toDto)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  public RoomDto createOrUpdateRoom(UUID roomId, RoomInputDto input) {
    var entity = roomId != null ? roomRepository.findById(roomId).orElseGet(Room::new) : new Room();

    entity.setNumber(input.getNumber());
    entity.setCapacity(input.getCapacity());

    var saved = roomRepository.save(entity);
    return toDto(saved);
  }

  public void deleteRoom(UUID roomId) {
    var entity =
        roomRepository
            .findById(roomId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    entity.setDeletedAt(Instant.now());
    roomRepository.save(entity);
  }

  public List<SeatDto> getSeats(UUID roomId) {
    if (roomRepository.findById(roomId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return seatRepository.findByRoomId(roomId).stream().map(this::toSeatDto).toList();
  }

  public SeatDto createOrUpdateSeat(UUID roomId, SeatInputDto input) {
    var room =
        roomRepository
            .findById(roomId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    var seat = new Seat();
    seat.setRoom(room);
    seat.setNumber(input.getNumber());
    var saved = seatRepository.save(seat);
    return toSeatDto(saved);
  }

  private RoomDto toDto(Room entity) {
    return new RoomDto(entity.getId(), entity.getNumber(), entity.getCapacity());
  }

  private SeatDto toSeatDto(Seat entity) {
    var roomId = entity.getRoom() != null ? entity.getRoom().getId() : null;
    return new SeatDto(entity.getId(), entity.getNumber(), roomId);
  }
}
