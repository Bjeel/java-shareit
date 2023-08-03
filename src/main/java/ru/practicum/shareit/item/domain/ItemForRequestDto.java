package ru.practicum.shareit.item.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ItemForRequestDto {
  @NotNull
  private Long id;

  @NotBlank(groups = ItemMarker.OnCreate.class)
  private String name;

  @NotBlank(groups = ItemMarker.OnCreate.class)
  private String description;

  @NotNull(groups = ItemMarker.OnCreate.class)
  private Boolean available;

  private Long owner;
}
