package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.domain.ItemBookingsDto;
import ru.practicum.shareit.item.domain.ItemDto;

import java.util.List;

public interface ItemService {
  ItemDto create(ItemDto item);

  ItemBookingsDto finOne(Long itemId, Long userId);

  List<ItemBookingsDto> findAll(Long userId);

  List<ItemDto> search(String text);

  ItemDto update(ItemDto item);

  void delete(Long itemId);
}
