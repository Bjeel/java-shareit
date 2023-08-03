package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.domain.BookingDto;
import ru.practicum.shareit.booking.domain.BookingFullDto;

import java.util.List;

public interface BookingService {
  BookingFullDto create(BookingDto bookingNewDto);

  BookingFullDto approve(Long id, Boolean approved, Long userId);

  BookingFullDto findById(Long id, Long userId);

  List<BookingFullDto> findAllByState(String state, Long userId);

  List<BookingFullDto> findAllByStateForOwner(String st, Long userId);
}
