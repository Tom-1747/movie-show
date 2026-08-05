package com.movie.demo.endpoint.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.demo.endpoint.rest.model.ProjectionRequest;
import com.movie.demo.endpoint.rest.model.ProjectionResponse;
import com.movie.demo.endpoint.rest.model.SeatAvailabilityResponse;
import com.movie.demo.service.ProjectionService;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(controllers = ProjectionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RestExceptionHandler.class)
class ProjectionControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private ProjectionService projectionService;

  private final UUID projectionId = UUID.fromString("11111111-1111-1111-1111-111111111111");
  private final UUID movieId = UUID.fromString("22222222-2222-2222-2222-222222222222");
  private final UUID roomId = UUID.fromString("33333333-3333-3333-3333-333333333333");
  private final UUID seatId = UUID.fromString("44444444-4444-4444-4444-444444444444");
  private final Instant datetime = Instant.parse("2026-08-10T18:30:00Z");

  @Test
  void list_returnsProjections() throws Exception {
    when(projectionService.list())
        .thenReturn(
            List.of(
                new ProjectionResponse(
                    projectionId, datetime, new BigDecimal("12.50"), movieId, roomId)));

    mockMvc
        .perform(get("/projections"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(projectionId.toString()))
        .andExpect(jsonPath("$[0].movieId").value(movieId.toString()))
        .andExpect(jsonPath("$[0].roomId").value(roomId.toString()))
        .andExpect(jsonPath("$[0].seatPrice").value(12.50));
  }

  @Test
  void getById_returnsProjection() throws Exception {
    when(projectionService.getById(projectionId))
        .thenReturn(
            new ProjectionResponse(
                projectionId, datetime, new BigDecimal("12.50"), movieId, roomId));

    mockMvc
        .perform(get("/projections/{projectionId}", projectionId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(projectionId.toString()))
        .andExpect(jsonPath("$.datetime").value(datetime.toString()));
  }

  @Test
  void getById_whenMissing_returns404() throws Exception {
    when(projectionService.getById(projectionId))
        .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Projection not found"));

    mockMvc
        .perform(get("/projections/{projectionId}", projectionId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message").value("Projection not found"));
  }

  @Test
  void createOrUpdate_returnsProjection() throws Exception {
    ProjectionRequest request =
        new ProjectionRequest(datetime, new BigDecimal("15.00"), movieId, roomId);
    when(projectionService.createOrUpdate(any(ProjectionRequest.class)))
        .thenReturn(
            new ProjectionResponse(
                projectionId, datetime, new BigDecimal("15.00"), movieId, roomId));

    mockMvc
        .perform(
            put("/projections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(projectionId.toString()))
        .andExpect(jsonPath("$.seatPrice").value(15.00));
  }

  @Test
  void delete_returnsNoContent() throws Exception {
    doNothing().when(projectionService).delete(projectionId);

    mockMvc
        .perform(delete("/projections/{projectionId}", projectionId))
        .andExpect(status().isNoContent());

    verify(projectionService).delete(eq(projectionId));
  }

  @Test
  void delete_whenMissing_returns404() throws Exception {
    doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Projection not found"))
        .when(projectionService)
        .delete(projectionId);

    mockMvc
        .perform(delete("/projections/{projectionId}", projectionId))
        .andExpect(status().isNotFound());
  }

  @Test
  void getSeats_returnsAvailability() throws Exception {
    when(projectionService.getSeats(projectionId))
        .thenReturn(List.of(new SeatAvailabilityResponse(seatId, "A1", roomId, true)));

    mockMvc
        .perform(get("/projections/{projectionId}/seats", projectionId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(seatId.toString()))
        .andExpect(jsonPath("$[0].number").value("A1"))
        .andExpect(jsonPath("$[0].roomId").value(roomId.toString()))
        .andExpect(jsonPath("$[0].available").value(true));
  }
}
