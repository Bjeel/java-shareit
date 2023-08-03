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
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
  private final BookingService bookingService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public BookingFullDto create(@Valid @RequestBody BookingDto bookingNewDto,
                               @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    bookingNewDto.setBooker(userId);

    return bookingService.create(bookingNewDto);
  }

  @GetMapping
  public List<BookingFullDto> findAll(@NotNull @RequestHeader(Headers.USER_ID) Long userId,
                                      @NotNull @RequestParam(defaultValue = "ALL", name = "state") String state,
                                      @RequestParam(defaultValue = "0", required = false, name = "from") int from,
                                      @RequestParam(defaultValue = "10", required = false, name = "size") int size) {
    return bookingService.findAllByState(state, userId, from, size);
  }

  @GetMapping("/{id}")
  public BookingFullDto findByBooker(@PathVariable Long id, @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    return bookingService.findById(id, userId);
  }

  @GetMapping("/owner")
  public List<BookingFullDto> findAllByOwner(@NotNull @RequestHeader(Headers.USER_ID) Long userId,
                                             @NotNull @RequestParam(defaultValue = "ALL", name = "state") String state,
                                             @RequestParam(defaultValue = "0", required = false, name = "from") int from,
                                             @RequestParam(defaultValue = "10", required = false, name = "size") int size) {

    return bookingService.findAllByStateForOwner(state, userId, from, size);
  }

  @PatchMapping("/{id}")
  public BookingFullDto approve(@PathVariable Long id,
                                @NotNull @RequestParam(required = true, name = "approved") Boolean approved,
                                @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    return bookingService.approve(id, approved, userId);
  }
}
