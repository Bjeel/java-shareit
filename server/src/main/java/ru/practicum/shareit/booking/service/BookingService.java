package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.domain.BookingDto;
import ru.practicum.shareit.booking.domain.BookingFullDto;

import java.util.List;

public interface BookingService {
  BookingFullDto create(BookingDto bookingNewDto);

  BookingFullDto approve(Long id, Boolean approved, Long userId);

  BookingFullDto findById(Long id, Long userId);

  List<BookingFullDto> findAllByState(State state, Long userId, int from, int size);

  List<BookingFullDto> findAllByStateForOwner(State st, Long userId, int from, int size);
}
