package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.item.domain.ItemDto;
import ru.practicum.shareit.item.domain.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
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
    userRepository.findOne(item.getOwner());
    Item createdItem = itemRepository.create(ItemMapper.toItemFromDto(item));

    return ItemMapper.toItemDto(createdItem);
  }

  @Override
  public ItemDto finOne(Long itemId) {
    Item item = itemRepository.findOne(itemId);

    return ItemMapper.toItemDto(item);
  }

  @Override
  public List<ItemDto> findAll(Long userId, String text) {
    List<Item> items = itemRepository.findAll(userId);

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

    List<Item> items = itemRepository.findAll(text);

    return items
      .stream()
      .map(ItemMapper::toItemDto)
      .collect(Collectors.toList());
  }

  @Override
  public ItemDto update(@NotNull ItemDto item) {
    userRepository.findOne(item.getOwner());
    Item updatedItem = itemRepository.update(ItemMapper.toItemFromDto(item));

    return ItemMapper.toItemDto(updatedItem);
  }

  @Override
  public String delete(Long itemId) {
    return itemRepository.delete(itemId) ? "Айтем удален" : "Айтем нельзя удалить";
  }
}
