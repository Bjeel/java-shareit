package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.booking.domain.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comments.domain.Comment;
import ru.practicum.shareit.comments.domain.CommentDto;
import ru.practicum.shareit.comments.domain.CommentMapper;
import ru.practicum.shareit.comments.domain.CommentNewDto;
import ru.practicum.shareit.comments.repository.CommentRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.item.domain.ItemDto;
import ru.practicum.shareit.item.domain.ItemFullDto;
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
  private final CommentRepository commentRepository;

  @Autowired
  public ItemServiceImpl(ItemRepository itemRepository,
                         UserRepository userRepository,
                         BookingRepository bookingRepository,
                         CommentRepository commentRepository) {
    this.itemRepository = itemRepository;
    this.userRepository = userRepository;
    this.bookingRepository = bookingRepository;
    this.commentRepository = commentRepository;
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
  public ItemFullDto finOne(Long itemId, Long userId) {
    Optional<Item> optionalItem = itemRepository.findById(itemId);

    if (optionalItem.isEmpty()) {
      throw new EntityNotFoundException("Item не найден");
    }

    Item item = optionalItem.get();

    ItemFullDto itemFullDto = ItemMapper.toItemBookingsDto(item);

    if (userId.equals(item.getOwner())) {
      addBooking(item, itemFullDto);
    }

    return itemFullDto;

  }

  @Override
  public List<ItemFullDto> findAll(Long userId) {
    List<Item> items = itemRepository.findAllByOwner(userId);

    return items
      .stream()
      .map(item -> {

        ItemFullDto itemFullDto = ItemMapper.toItemBookingsDto(item);

        if (userId.equals(item.getOwner())) {
          addBooking(item, itemFullDto);
        }

        return itemFullDto;
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

    if (optionalItem.isEmpty()) {
      throw new EntityNotFoundException("Item отсутствует");
    }

    Item updatedItem = optionalItem.get();

    updatedItem = ItemMapper.toUpdatedItem(item, updatedItem);

    return ItemMapper.toItemDto(itemRepository.save(updatedItem));
  }

  @Override
  public void delete(Long itemId) {
    itemRepository.deleteById(itemId);
  }

  @Override
  public CommentNewDto addComment(CommentDto commentDto) {
    Optional<User> optionalUser = userRepository.findById(commentDto.getAuthorId());

    if (optionalUser.isEmpty()) {
      throw new EntityNotFoundException("Владелец отсутствуте");
    }

    Optional<Item> optionalItem = itemRepository.findById(commentDto.getItemId());

    if (optionalItem.isEmpty()) {
      throw new EntityNotFoundException("Item отсутствует");
    }

    Optional<Booking> optionalBooking = bookingRepository.findByItemAndBookerAndEndAfter(optionalItem.get(), optionalUser.get(), LocalDateTime.now());

    if (optionalBooking.isEmpty()) {
      throw new EntityNotFoundException("Нет подходящей аренды");
    }

    Comment newComment = new Comment();

    newComment.setText(commentDto.getText());
    newComment.setCreated(LocalDateTime.now());
    newComment.setAuthor(optionalUser.get());
    newComment.setItem(optionalItem.get());

    Comment comment = commentRepository.save(newComment);

    return CommentMapper.toCommentNewDto(comment);
  }

  private void addBooking(Item item, ItemFullDto itemFullDto) {
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

    itemFullDto.setLastBooking(BookingMapper.toBookingItemDto(prevBooking[0]));
    itemFullDto.setNextBooking(BookingMapper.toBookingItemDto(nextBooking[0]));
  }
}
