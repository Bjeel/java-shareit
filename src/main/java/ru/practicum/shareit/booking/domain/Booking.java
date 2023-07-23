package ru.practicum.shareit.booking.domain;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.domain.User;

import java.time.LocalDate;

@Data
@Builder
public class Booking {
  private Long id;
  private LocalDate start;
  private LocalDate end;
  private Item item;
  private User booker;
  private Status status;
}
