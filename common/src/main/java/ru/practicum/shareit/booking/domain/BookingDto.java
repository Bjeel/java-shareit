package ru.practicum.shareit.booking.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
  private Long id;

  private Long booker;

  private Long itemId;

  @FutureOrPresent
  @NotNull
  private LocalDateTime start;

  @Future
  @NotNull
  private LocalDateTime end;
}
