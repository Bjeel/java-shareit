package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.domain.BookingDto;
import ru.practicum.shareit.booking.domain.BookingNewDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
  public BookingNewDto create(@Valid @RequestBody BookingDto bookingNewDto, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
    System.out.println(bookingNewDto);
    bookingNewDto.setBooker(userId);

    return bookingService.create(bookingNewDto);
  }

}
