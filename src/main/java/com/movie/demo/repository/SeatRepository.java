package com.movie.demo.repository;

import com.movie.demo.domain.Seat;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {
  List<Seat> findByRoomId(UUID roomId);
}
