package ru.practicum.shareit.booking.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class BookingItemDto {
  @NotNull
  private Long id;

  @NotNull
  private Long bookerId;

  @NotNull
  private Long itemId;
}
