package ru.practicum.shareit.item.domain;

import ru.practicum.shareit.exception.EntityNotFoundException;

public class ItemMapper {
  public static ItemDto toItemDto(Item item) {
    if (item == null) {
      throw new EntityNotFoundException("Item не может быть null");
    }

    return ItemDto
      .builder()
      .id(item.getId())
      .name(item.getName())
      .description(item.getDescription())
      .available(item.getAvailable())
      .owner(item.getOwner())
      .build();
  }

  public static Item toItemFromDto(ItemDto item) {
    if (item == null) {
      throw new EntityNotFoundException("Item не может быть null");
    }

    return new Item();
  }
}
