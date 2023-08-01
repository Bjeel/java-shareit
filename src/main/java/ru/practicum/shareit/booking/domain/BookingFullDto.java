package ru.practicum.shareit.booking.domain;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.domain.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingFullDto {
  @NotNull
  Long id;

  @NotNull
  User booker;

  @NotNull
  Item item;

  @FutureOrPresent
  LocalDateTime start;

  @FutureOrPresent
  LocalDateTime end;

  @NotNull
  Status status;
}
