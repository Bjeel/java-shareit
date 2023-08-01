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
  @NotNull
  private Long id;

  @NotBlank
  private String name;

  @NotBlank
  private String description;

  @NotNull
  private Boolean available;

  @NotNull
  private Long owner;

  private BookingItemDto lastBooking;
  private BookingItemDto nextBooking;

  private List<CommentNewDto> comments;
}
