package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comments.domain.CommentDto;
import ru.practicum.shareit.comments.domain.CommentNewDto;
import ru.practicum.shareit.item.domain.ItemDto;
import ru.practicum.shareit.item.domain.ItemFullDto;

import java.util.List;

public interface ItemService {
  ItemDto create(ItemDto item);

  ItemFullDto findOne(Long itemId, Long userId);

  List<ItemFullDto> findAll(Long userId);

  List<ItemDto> search(String text);

  ItemDto update(ItemDto item);

  void delete(Long itemId);

  CommentNewDto addComment(CommentDto commentDto);
}
