package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.item.domain.ItemDto;
import ru.practicum.shareit.item.domain.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
  private final ItemRepository itemRepository;
  private final UserRepository userRepository;

  @Autowired
  public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
    this.itemRepository = itemRepository;
    this.userRepository = userRepository;
  }

  @Override
  public ItemDto create(@NotNull ItemDto item) {
    Optional<User> optionalUser = userRepository.findById(item.getOwner());

    if (optionalUser.isEmpty()) {
      throw new EntityNotFoundException("Нет пользоателя для которого создается Item");
    }

    Item createdItem = itemRepository.save(ItemMapper.toItemFromDto(item));

    return ItemMapper.toItemDto(createdItem);
  }

  @Override
  public ItemDto finOne(Long itemId) {
    Optional<Item> optionalItem = itemRepository.findById(itemId);

    if (optionalItem.isPresent()) {
      return ItemMapper.toItemDto(optionalItem.get());
    }

    throw new EntityNotFoundException("Item не найден");
  }

  @Override
  public List<ItemDto> findAll(Long userId, String text) {
    List<Item> items = itemRepository.findAllByOwner(userId);

    return items
      .stream()
      .map(ItemMapper::toItemDto)
      .collect(Collectors.toList());
  }

  @Override
  public List<ItemDto> search(String text) {
    if (text != null && text.length() == 0) {
      return new ArrayList<>();
    }

    List<Item> items = itemRepository.search(text);

    return items
      .stream()
      .map(ItemMapper::toItemDto)
      .collect(Collectors.toList());
  }

  @Override
  public ItemDto update(@NotNull ItemDto item) {
    Optional<User> optionalUser = userRepository.findById(item.getOwner());

    if (optionalUser.isEmpty()) {
      throw new EntityNotFoundException("Владелец отсутствуте");
    }

    Optional<Item> optionalItem = itemRepository.findById(item.getId());

    if (optionalItem.isPresent()) {
      Item updatedItem = optionalItem.get();

      updatedItem = ItemMapper.toUpdatedItem(item, updatedItem);

      return ItemMapper.toItemDto(itemRepository.save(updatedItem));
    }

    throw new EntityNotFoundException("Item отсутствует");
  }

  @Override
  public void delete(Long itemId) {
    itemRepository.deleteById(itemId);
  }
}
