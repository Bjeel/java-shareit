package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.domain.BookingDto;
import ru.practicum.shareit.booking.domain.BookingNewDto;

public interface BookingService {
  BookingNewDto create(BookingDto bookingNewDto);
}
