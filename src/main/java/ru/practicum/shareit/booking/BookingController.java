package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.domain.BookingDto;
import ru.practicum.shareit.booking.domain.BookingFullDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
  private final BookingService bookingService;

  @Autowired
  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public BookingFullDto create(@Valid @RequestBody BookingDto bookingNewDto, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
    bookingNewDto.setBooker(userId);

    return bookingService.create(bookingNewDto);
  }

  @GetMapping
  public List<BookingFullDto> findAll(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam("state") Optional<String> state) {
    return bookingService.findAllByState(state.orElse("ALL"), userId);
  }

  @GetMapping("/{id}")
  public BookingFullDto findByBooker(@PathVariable Long id, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
    return bookingService.findById(id, userId);
  }

  @GetMapping("/owner")
  public List<BookingFullDto> findAllByOwner(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam("state") Optional<String> state) {
    return bookingService.findAllByStateForOwner(state.orElse("ALL"), userId);
  }

  @PatchMapping("/{id}")
  public BookingFullDto approve(@PathVariable Long id, @NotNull @RequestParam("approved") Boolean approved, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
    return bookingService.approve(id, approved, userId);
  }
}
