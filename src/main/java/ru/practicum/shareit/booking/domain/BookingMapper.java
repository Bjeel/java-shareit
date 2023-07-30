package ru.practicum.shareit.booking.domain;

import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.domain.User;

public class BookingMapper {
  public static Booking toBooking(BookingDto bookingNewDto, Item item, User user) {
    Booking booking = new Booking();

    booking.setId(bookingNewDto.getId());
    booking.setStart(bookingNewDto.getStart());
    booking.setEnd(bookingNewDto.getEnd());
    booking.setItem(item);
    booking.setBooker(user);
    booking.setStatus(Status.WAITING);

    return booking;
  }

  public static BookingFullDto toFullDto(Booking save) {
    return BookingFullDto.builder()
      .id(save.getId())
      .end(save.getEnd())
      .start(save.getStart())
      .item(save.getItem())
      .booker(save.getBooker())
      .status(save.getStatus())
      .build();
  }

  public static BookingItemDto toBookingItemDto(Booking save) {
    if (save == null) {
      return null;
    }

    return BookingItemDto.builder()
      .id(save.getId())
      .itemId(save.getItem().getId())
      .bookerId(save.getBooker().getId())
      .build();
  }
}
