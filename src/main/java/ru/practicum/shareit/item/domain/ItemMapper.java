package ru.practicum.shareit.item.domain;

import ru.practicum.shareit.comments.domain.CommentMapper;
import ru.practicum.shareit.exception.EntityNotFoundException;

import java.util.stream.Collectors;

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

    Item newItem = new Item();

    newItem.setId(item.getId());
    newItem.setAvailable(item.getAvailable());
    newItem.setName(item.getName());
    newItem.setDescription(item.getDescription());
    newItem.setOwner(item.getOwner());
    newItem.setRequest(newItem.getRequest());

    return newItem;
  }

  public static ItemFullDto toItemBookingsDto(Item item) {
    return ItemFullDto
      .builder()
      .id(item.getId())
      .name(item.getName())
      .description(item.getDescription())
      .available(item.getAvailable())
      .owner(item.getOwner())
      .lastBooking(null)
      .nextBooking(null)
      .comments(item.getComments().stream().map(CommentMapper::toCommentNewDto).collect(Collectors.toList()))
      .build();
  }

  public static Item toUpdatedItem(ItemDto item, Item updatedItem) {
    if (item == null || updatedItem == null) {
      throw new EntityNotFoundException("Item не может быть null");
    }

    if (item.getId() != null) {
      updatedItem.setId(item.getId());
    }

    if (item.getAvailable() != null) {
      updatedItem.setAvailable(item.getAvailable());
    }

    if (item.getName() != null) {
      updatedItem.setName(item.getName());
    }

    if (item.getDescription() != null) {
      updatedItem.setDescription(item.getDescription());
    }

    if (item.getOwner() != null) {
      updatedItem.setOwner(item.getOwner());
    }

    return updatedItem;
  }
}
