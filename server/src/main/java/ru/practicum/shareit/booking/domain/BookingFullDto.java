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
  private Long id;

  @NotNull
  private User booker;

  @NotNull
  private Item item;

  @FutureOrPresent
  private LocalDateTime start;

  @FutureOrPresent
  private LocalDateTime end;

  @NotNull
  private Status status;
}
