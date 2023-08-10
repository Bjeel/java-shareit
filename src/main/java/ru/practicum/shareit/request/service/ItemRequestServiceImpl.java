package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.request.domain.ItemRequest;
import ru.practicum.shareit.request.domain.ItemRequestDto;
import ru.practicum.shareit.request.domain.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {
  private final ItemRequestRepository itemRequestRepository;
  private final UserRepository userRepository;

  @Override
  public ItemRequestDto create(ItemRequestDto itemRequestDto) {
    userRepository.findById(itemRequestDto.getRequester()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

    ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.toItemRequest(itemRequestDto));

    return ItemRequestMapper.toDto(itemRequest);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ItemRequestDto> findAllByRequester(Long userId) {
    userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

    List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequester(userId);

    return itemRequestList.stream().map(ItemRequestMapper::toDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  @Override
  public List<ItemRequestDto> findALlPageable(Long userId, int from, int size) {
    userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Нет прав для просмотра"));

    PageRequest page = PageRequest.of(from / size, size);

    return itemRequestRepository.findAllByRequesterIsNot(page, userId)
      .stream()
      .map(ItemRequestMapper::toDto)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  @Override
  public ItemRequestDto finById(Long userId, Long id) {
    userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Нет прав для просмотра"));

    return ItemRequestMapper.toDto(itemRequestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Нет данного запроса")));
  }
}
