package ru.practicum.shareit.booking.domain;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.domain.User;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingFullDto {
  Long id;

  User booker;

  Item item;

  @Future
  LocalDateTime start;

  LocalDateTime end;

  Status status;
}
