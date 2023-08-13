package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.domain.BookingDto;
import ru.practicum.shareit.booking.domain.BookingFullDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.consts.Headers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
  private final BookingService bookingService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public BookingFullDto create(@RequestBody BookingDto bookingNewDto,
                               @RequestHeader(Headers.USER_ID) Long userId) {
    bookingNewDto.setBooker(userId);

    return bookingService.create(bookingNewDto);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public List<BookingFullDto> findAllByState(@RequestHeader(Headers.USER_ID) Long userId,
                                             @RequestParam(defaultValue = "ALL", name = "state") State state,
                                             @RequestParam(defaultValue = "0", name = "from") int from,
                                             @RequestParam(defaultValue = "10", name = "size") int size) {
    return bookingService.findAllByState(state, userId, from, size);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{id}")
  public BookingFullDto findById(@PathVariable Long id, @RequestHeader(Headers.USER_ID) Long userId) {
    return bookingService.findById(id, userId);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/owner")
  public List<BookingFullDto> findAllByStateForOwner(@RequestHeader(Headers.USER_ID) Long userId,
                                                     @RequestParam(defaultValue = "ALL", name = "state") State state,
                                                     @RequestParam(defaultValue = "0", name = "from") int from,
                                                     @RequestParam(defaultValue = "10", name = "size") int size) {

    return bookingService.findAllByStateForOwner(state, userId, from, size);
  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/{id}")
  public BookingFullDto approve(@PathVariable Long id,
                                @RequestParam(name = "approved") Boolean approved,
                                @RequestHeader(Headers.USER_ID) Long userId) {
    return bookingService.approve(id, approved, userId);
  }
}
