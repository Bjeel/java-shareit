package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.domain.ItemDto;

import java.util.List;

public interface ItemService {
  ItemDto create(ItemDto item);

  ItemDto finOne(Long itemId);

  List<ItemDto> findAll(Long userId, String text);

  List<ItemDto> search(String text);

  ItemDto update(ItemDto item);

  String delete(Long itemId);
}
