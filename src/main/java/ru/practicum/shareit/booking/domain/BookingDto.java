package ru.practicum.shareit.booking.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
  Long id;

  Long booker;

  Long itemId;

  @FutureOrPresent
  @NotNull
  LocalDateTime start;

  @FutureOrPresent
  @NotNull
  LocalDateTime end;
}
