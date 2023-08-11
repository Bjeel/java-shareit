package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.domain.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
  ItemRequestDto create(ItemRequestDto itemRequestDto);

  List<ItemRequestDto> findAllByRequester(Long userId);

  List<ItemRequestDto> findALlPageable(Long userId, int from, int size);

  ItemRequestDto finById(Long userId, Long id);
}
