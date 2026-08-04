package com.movie.demo.endpoint.rest.controller;

import com.movie.demo.endpoint.rest.model.RoomDto;
import com.movie.demo.endpoint.rest.model.RoomInputDto;
import com.movie.demo.endpoint.rest.model.SeatDto;
import com.movie.demo.endpoint.rest.model.SeatInputDto;
import com.movie.demo.service.RoomService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RoomController {

  private final RoomService roomService;

  @GetMapping("/rooms")
  @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER')")
  public ResponseEntity<List<RoomDto>> getRooms() {
    return ResponseEntity.ok(roomService.getRooms());
  }

  @GetMapping("/rooms/{roomId}")
  @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER')")
  public ResponseEntity<RoomDto> getRoom(@PathVariable UUID roomId) {
    return ResponseEntity.ok(roomService.getRoom(roomId));
  }

  @PutMapping("/rooms")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<RoomDto> createOrUpdateRoom(
      @RequestParam(required = false) UUID roomId, @RequestBody RoomInputDto input) {
    return ResponseEntity.ok(roomService.createOrUpdateRoom(roomId, input));
  }

  @DeleteMapping("/rooms/{roomId}")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<Void> deleteRoom(@PathVariable UUID roomId) {
    roomService.deleteRoom(roomId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/rooms/{roomId}/seats")
  @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER')")
  public ResponseEntity<List<SeatDto>> getSeats(@PathVariable UUID roomId) {
    return ResponseEntity.ok(roomService.getSeats(roomId));
  }

  @PutMapping("/rooms/{roomId}/seats")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<SeatDto> createOrUpdateSeat(
      @PathVariable UUID roomId, @RequestBody SeatInputDto input) {
    return ResponseEntity.ok(roomService.createOrUpdateSeat(roomId, input));
  }
}
