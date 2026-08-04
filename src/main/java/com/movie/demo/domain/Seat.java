package com.movie.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String number;

  @ManyToOne
  @JoinColumn(name = "room_id", nullable = false)
  private Room room;

  /** UML association User --take--> Seat (inverse side). */
  @OneToOne(mappedBy = "seat")
  @JsonIgnore
  private User takenBy;

  @ManyToMany(mappedBy = "seats")
  @JsonIgnore
  @Builder.Default
  private List<Reservation> reservations = new ArrayList<>();
}
