package com.movie.demo.endpoint.rest.controller.rest.dto;

import com.movie.demo.domain.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatAvailability {
  private Seat seat;
  private boolean available;
}
