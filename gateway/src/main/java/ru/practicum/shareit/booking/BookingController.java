package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.domain.BookingDto;
import ru.practicum.shareit.booking.domain.BookingState;
import ru.practicum.shareit.consts.Headers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
  private final BookingClient bookingClient;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public ResponseEntity<Object> getBookings(@RequestHeader(Headers.USER_ID) long userId,
                                            @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
    BookingState state = BookingState.from(stateParam)
      .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown state: %s", stateParam)));
    log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
    return bookingClient.getBookings(userId, state, from, size);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/owner")
  public ResponseEntity<Object> getOwnerBookings(@RequestHeader(Headers.USER_ID) long userId,
                                                 @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
    BookingState state = BookingState.from(stateParam)
      .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown state: %s", stateParam)));
    log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
    return bookingClient.getOwnerBookings(userId, state, from, size);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public ResponseEntity<Object> bookItem(@RequestHeader(Headers.USER_ID) long userId,
                                         @RequestBody @Valid BookingDto requestDto) {
    log.info("Creating booking {}, userId={}", requestDto, userId);
    return bookingClient.bookItem(userId, requestDto);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{bookingId}")
  public ResponseEntity<Object> getBooking(@RequestHeader(Headers.USER_ID) long userId,
                                           @PathVariable Long bookingId) {
    log.info("Get booking {}, userId={}", bookingId, userId);
    return bookingClient.getBooking(userId, bookingId);
  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/{bookingId}")
  public ResponseEntity<Object> approve(@PathVariable Long bookingId,
                                        @NotNull @RequestParam(name = "approved") Boolean approved,
                                        @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    log.info("Approve booking {}, userId={}, approve={}", bookingId, userId, approved);
    return bookingClient.approve(bookingId, approved, userId);
  }
}
