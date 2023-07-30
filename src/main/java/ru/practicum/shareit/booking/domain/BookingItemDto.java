package ru.practicum.shareit.booking.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingItemDto {
  Long id;

  Long bookerId;

  Long itemId;
}
