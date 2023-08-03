package ru.practicum.shareit.booking.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class BookingItemDto {
  @NotNull
  Long id;

  @NotNull
  Long bookerId;

  @NotNull
  Long itemId;
}
