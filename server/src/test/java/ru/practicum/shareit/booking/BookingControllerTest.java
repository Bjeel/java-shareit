package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.booking.domain.BookingDto;
import ru.practicum.shareit.booking.domain.BookingFullDto;

import ru.practicum.shareit.booking.domain.Status;
import ru.practicum.shareit.booking.service.BookingService;

import ru.practicum.shareit.consts.Headers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private BookingService bookingService;

  @Autowired
  ObjectMapper mapper;

  BookingFullDto bookingFullDto = BookingFullDto
    .builder()
    .id(1L)
    .booker(null)
    .item(null)
    .end(LocalDateTime.parse("2025-08-04T13:26:59"))
    .start(LocalDateTime.parse("2025-08-03T13:26:59"))
    .status(Status.WAITING)
    .build();

  BookingDto bookingDto = BookingDto
    .builder()
    .id(1L)
    .booker(1L)
    .end(LocalDateTime.parse("2025-08-04T13:26:59"))
    .start(LocalDateTime.parse("2025-08-03T13:26:59"))
    .build();

  @BeforeEach
  void setUp(WebApplicationContext wac) {
    mvc = MockMvcBuilders
      .webAppContextSetup(wac)
      .build();

    bookingFullDto = BookingFullDto
      .builder()
      .id(1L)
      .booker(null)
      .item(null)
      .end(LocalDateTime.parse("2025-08-04T13:26:59"))
      .start(LocalDateTime.parse("2025-08-03T13:26:59"))
      .status(Status.WAITING)
      .build();

    bookingDto = BookingDto
      .builder()
      .id(1L)
      .booker(1L)
      .end(LocalDateTime.parse("2025-08-04T13:26:59"))
      .start(LocalDateTime.parse("2025-08-03T13:26:59"))
      .build();
  }

  @Test
  void create() throws Exception {
    when(bookingService.create(any()))
      .thenReturn(bookingFullDto);

    mvc.perform(post("/bookings")
        .content(mapper.writeValueAsString(bookingDto))
        .characterEncoding(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_JSON)
        .header(Headers.USER_ID, 1L)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().is(201))
      .andExpect(jsonPath("$.id", is(bookingFullDto.getId()), Long.class))
      .andExpect(jsonPath("$.booker", is(bookingFullDto.getBooker())))
      .andExpect(jsonPath("$.item", is(bookingFullDto.getItem())))
      .andExpect(jsonPath("$.start", is(bookingFullDto.getStart().toString())))
      .andExpect(jsonPath("$.end", is(bookingFullDto.getEnd().toString())))
      .andExpect(jsonPath("$.status", is(Status.WAITING.toString())));
  }

  @Test
  void findAll() throws Exception {
    when(bookingService.findAllByState(any(), anyLong(), anyInt(), anyInt()))
      .thenReturn(List.of(bookingFullDto));

    mvc.perform(get("/bookings")
        .contentType(MediaType.APPLICATION_JSON)
        .header(Headers.USER_ID, 1L)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].id", is(bookingFullDto.getId()), Long.class))
      .andExpect(jsonPath("$[0].booker", is(bookingFullDto.getBooker())))
      .andExpect(jsonPath("$[0].item", is(bookingFullDto.getItem())))
      .andExpect(jsonPath("$[0].start", is(bookingFullDto.getStart().toString())))
      .andExpect(jsonPath("$[0].end", is(bookingFullDto.getEnd().toString())))
      .andExpect(jsonPath("$[0].status", is(Status.WAITING.toString())));
  }

  @Test
  void findByBooker() throws Exception {
    when(bookingService.findById(anyLong(), anyLong()))
      .thenReturn(bookingFullDto);

    mvc.perform(get("/bookings/1")
        .contentType(MediaType.APPLICATION_JSON)
        .header(Headers.USER_ID, 1L)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(bookingFullDto.getId()), Long.class))
      .andExpect(jsonPath("$.booker", is(bookingFullDto.getBooker())))
      .andExpect(jsonPath("$.item", is(bookingFullDto.getItem())))
      .andExpect(jsonPath("$.start", is(bookingFullDto.getStart().toString())))
      .andExpect(jsonPath("$.end", is(bookingFullDto.getEnd().toString())))
      .andExpect(jsonPath("$.status", is(Status.WAITING.toString())));
  }

  @Test
  void findAllByOwner() throws Exception {
    when(bookingService.findAllByStateForOwner(any(), anyLong(), anyInt(), anyInt()))
      .thenReturn(List.of(bookingFullDto));

    mvc.perform(get("/bookings/owner")
        .contentType(MediaType.APPLICATION_JSON)
        .header(Headers.USER_ID, 1L)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].id", is(bookingFullDto.getId()), Long.class))
      .andExpect(jsonPath("$[0].booker", is(bookingFullDto.getBooker())))
      .andExpect(jsonPath("$[0].item", is(bookingFullDto.getItem())))
      .andExpect(jsonPath("$[0].start", is(bookingFullDto.getStart().toString())))
      .andExpect(jsonPath("$[0].end", is(bookingFullDto.getEnd().toString())))
      .andExpect(jsonPath("$[0].status", is(Status.WAITING.toString())));
  }

  @Test
  void approve() throws Exception {
    bookingFullDto.setStatus(Status.APPROVED);
    when(bookingService.approve(anyLong(), anyBoolean(), anyLong()))
      .thenReturn(bookingFullDto);

    mvc.perform(patch("/bookings/1?approved=true")
        .contentType(MediaType.APPLICATION_JSON)
        .header(Headers.USER_ID, 1L)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(bookingFullDto.getId()), Long.class))
      .andExpect(jsonPath("$.booker", is(bookingFullDto.getBooker())))
      .andExpect(jsonPath("$.item", is(bookingFullDto.getItem())))
      .andExpect(jsonPath("$.start", is(bookingFullDto.getStart().toString())))
      .andExpect(jsonPath("$.end", is(bookingFullDto.getEnd().toString())))
      .andExpect(jsonPath("$.status", is(Status.APPROVED.toString())));
  }
}
