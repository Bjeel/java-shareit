package ru.practicum.shareit.item.domain;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.domain.BookingItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemBookingsDto {
  private Long id;

  @NotBlank(groups = ItemMarker.OnCreate.class)
  private String name;

  @NotBlank(groups = ItemMarker.OnCreate.class)
  private String description;

  @NotNull(groups = ItemMarker.OnCreate.class)
  private Boolean available;

  private Long owner;

  private BookingItemDto lastBooking;
  private BookingItemDto nextBooking;
}
