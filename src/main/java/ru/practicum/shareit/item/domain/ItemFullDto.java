package ru.practicum.shareit.item.domain;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.domain.BookingItemDto;
import ru.practicum.shareit.comments.domain.CommentNewDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class ItemFullDto {
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

  private List<CommentNewDto> comments;
}
