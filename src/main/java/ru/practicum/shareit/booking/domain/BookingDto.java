package ru.practicum.shareit.booking.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
  Long id;

  Long booker;

  Long itemId;

  @Future
  LocalDateTime start;

  LocalDateTime end;
}
