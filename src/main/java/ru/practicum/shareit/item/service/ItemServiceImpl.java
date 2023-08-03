package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.exception.UnavailableAccessException;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.item.domain.ItemDto;
import ru.practicum.shareit.item.domain.ItemFullDto;
import ru.practicum.shareit.item.domain.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
  private final ItemRepository itemRepository;
  private final UserRepository userRepository;
  private final BookingRepository bookingRepository;
  private final CommentRepository commentRepository;

  @Override
  public ItemDto create(@NotNull ItemDto item) {
    userRepository.findById(item.getOwner()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

    Item createdItem = itemRepository.save(ItemMapper.toItemFromDto(item));

    log.info("Создан предмет: {}", item);
    return ItemMapper.toItemDto(createdItem);
  }

  @Override
  public ItemFullDto findOne(Long itemId, Long userId) {
    Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Предмет не найден"));

    ItemFullDto itemFullDto = ItemMapper.toItemBookingsDto(item);

    List<Comment> comments = commentRepository.findAllByItemId(item.getId());

    itemFullDto.setComments(comments.stream().map(CommentMapper::toCommentNewDto).collect(Collectors.toList()));

    if (userId.equals(item.getOwner())) {
      addBooking(item, itemFullDto);
    }

    log.info("Получение предмета: {}", item);
    return itemFullDto;

  }

  @Override
  public List<ItemFullDto> findAll(Long userId) {
    List<Item> items = itemRepository.findAllByOwner(userId);

    log.info("Получение всех предметов пользователя {}", userId);

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
    if (text != null && text.isBlank()) {
      return new ArrayList<>();
    }

    List<Item> items = itemRepository.search(text);

    log.info("Получение предметов в соответствие с поисковым запросом: {}", text);

    return items
      .stream()
      .map(ItemMapper::toItemDto)
      .collect(Collectors.toList());
  }

  @Override
  public ItemDto update(@NotNull ItemDto item) {
    userRepository.findById(item.getOwner()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

    Item updatedItem = itemRepository.findById(item.getId()).orElseThrow(() -> new EntityNotFoundException("Предмет не найден"));

    updatedItem = ItemMapper.toUpdatedItem(item, updatedItem);

    log.info("Оббновление предмета: {}", item);
    return ItemMapper.toItemDto(updatedItem);
  }

  @Override
  public void delete(Long itemId) {
    itemRepository.deleteById(itemId);
    log.info("Предмет id {} удален", itemId);
  }

  @Override
  public CommentNewDto addComment(CommentDto commentDto) {
    User user = userRepository.findById(commentDto.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

    Item item = itemRepository.findById(commentDto.getItemId()).orElseThrow(() -> new EntityNotFoundException("Предмет не найден"));

    Booking booking = bookingRepository.findTopByItemAndBookerAndStartIsBeforeOrderByStartDesc(item,
      user,
      LocalDateTime.now()
    ).orElseThrow(() -> new UnavailableAccessException("Аренда не найдена"));

    if (!booking.getStatus().equals(Status.APPROVED)) {
      throw new UnavailableAccessException("Нет подходящей аренды");
    }

    Comment newComment = CommentMapper.toComment(commentDto);

    newComment.setAuthor(user);
    newComment.setItemId(item.getId());

    Comment comment = commentRepository.save(newComment);

    log.info("Добавлен комментарий {} удален", commentDto);
    return CommentMapper.toCommentNewDto(comment);
  }

  private void addBooking(Item item, ItemFullDto itemFullDto) {
    List<Booking> bookingList = bookingRepository.findAllByItem(item);

    final Booking[] lastBooking = {null};
    final Booking[] nextBooking = {null};

    LocalDateTime currentDate = LocalDateTime.now();

    bookingList.forEach(booking -> {
      if (!booking.getStatus().equals(Status.APPROVED)) {
        return;
      }

      if (booking.getStart().isBefore(currentDate)) {
        if (lastBooking[0] == null) {
          lastBooking[0] = booking;
        } else {
          lastBooking[0] = booking.getStart().isAfter(lastBooking[0].getStart()) ? booking : lastBooking[0];
        }
      }

      if (booking.getStart().isAfter(currentDate)) {
        if (nextBooking[0] == null) {
          nextBooking[0] = booking;

        } else {
          nextBooking[0] = booking.getEnd().isBefore(nextBooking[0].getEnd()) ? booking : nextBooking[0];
        }
      }
    });

    itemFullDto.setLastBooking(BookingMapper.toBookingItemDto(lastBooking[0]));
    itemFullDto.setNextBooking(BookingMapper.toBookingItemDto(nextBooking[0]));

    log.info("Добавлен информация о последней и следующей арендах для предмета {}", item);
  }
}
