package ru.practicum.shareit.booking.domain;

import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.domain.User;

import java.time.LocalDateTime;

public class BookingMapper {
  public static Booking toBooking(BookingDto bookingNewDto, Item item, User user) {
    Booking booking = new Booking();

    booking.setId(bookingNewDto.getId());
    booking.setStart(bookingNewDto.getStart().toString());
    booking.setEnd(bookingNewDto.getEnd().toString());
    booking.setItem(item);
    booking.setBooker(user);
    booking.setStatus(Status.WAITING);

    return booking;
  }

  public static BookingNewDto toDtoFromNewBooking(Booking save, Item item) {
    return BookingNewDto.builder()
      .id(save.getId())
      .end(LocalDateTime.parse(save.getEnd()))
      .start(LocalDateTime.parse(save.getStart()))
      .item(item)
      .booker(save.getBooker())
      .status(save.getStatus())
      .build();
  }

  public static BookingNewDto toDto(Booking save) {
    return BookingNewDto.builder()
      .id(save.getId())
      .end(LocalDateTime.parse(save.getEnd()))
      .start(LocalDateTime.parse(save.getStart()))
      .item(save.getItem())
      .booker(save.getBooker())
      .status(save.getStatus())
      .build();
  }
}
