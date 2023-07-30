package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.booking.domain.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.item.domain.ItemBookingsDto;
import ru.practicum.shareit.item.domain.ItemDto;
import ru.practicum.shareit.item.domain.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
  private final ItemRepository itemRepository;
  private final UserRepository userRepository;
  private final BookingRepository bookingRepository;

  @Autowired
  public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, BookingRepository bookingRepository) {
    this.itemRepository = itemRepository;
    this.userRepository = userRepository;
    this.bookingRepository = bookingRepository;
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
  public ItemBookingsDto finOne(Long itemId, Long userId) {
    Optional<Item> optionalItem = itemRepository.findById(itemId);

    if (optionalItem.isEmpty()) {
      throw new EntityNotFoundException("Item не найден");
    }

    Item item = optionalItem.get();

    ItemBookingsDto itemBookingsDto = ItemMapper.toItemBookingsDto(item);

    if (userId.equals(item.getOwner())) {
      addBooking(item, itemBookingsDto);
    }

    return itemBookingsDto;

  }

  @Override
  public List<ItemBookingsDto> findAll(Long userId) {
    List<Item> items = itemRepository.findAllByOwner(userId);

    return items
      .stream()
      .map(item -> {

        ItemBookingsDto itemBookingsDto = ItemMapper.toItemBookingsDto(item);

        if (userId.equals(item.getOwner())) {
          addBooking(item, itemBookingsDto);
        }

        return itemBookingsDto;
      })
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

  private void addBooking(Item item, ItemBookingsDto itemBookingsDto) {
    List<Booking> bookingList = bookingRepository.findAllByItem(item);


    final Booking[] prevBooking = {null};
    final Booking[] nextBooking = {null};

    LocalDateTime currentDate = LocalDateTime.now();

    bookingList.forEach(booking -> {
      if (!booking.getStatus().equals(Status.APPROVED)) {
        return;
      }

      if (booking.getEnd().isBefore(currentDate)) {
        if (prevBooking[0] == null) {
          prevBooking[0] = booking;

          return;
        }

        prevBooking[0] = booking.getEnd().isAfter(prevBooking[0].getEnd()) ? booking : prevBooking[0];
      }

      if (booking.getStart().isAfter(currentDate)) {
        if (nextBooking[0] == null) {
          nextBooking[0] = booking;

          return;
        }

        nextBooking[0] = booking.getEnd().isBefore(nextBooking[0].getEnd()) ? booking : nextBooking[0];
      }
    });

    itemBookingsDto.setLastBooking(BookingMapper.toBookingItemDto(prevBooking[0]));
    itemBookingsDto.setNextBooking(BookingMapper.toBookingItemDto(nextBooking[0]));
  }
}
