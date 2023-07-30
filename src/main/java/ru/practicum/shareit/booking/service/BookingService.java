package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.domain.BookingDto;
import ru.practicum.shareit.booking.domain.BookingNewDto;

public interface BookingService {
  BookingNewDto create(BookingDto bookingNewDto);

  BookingNewDto approve(Long id, Boolean approved, Long userId);

  BookingNewDto findById(Long id, Long userId);
}
