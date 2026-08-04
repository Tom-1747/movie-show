package com.movie.demo.endpoint.rest.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomDto {
  private UUID id;
  private String number;
  private int capacity;
}
