package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.domain.Status;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.booking.domain.BookingDto;
import ru.practicum.shareit.booking.domain.BookingFullDto;
import ru.practicum.shareit.booking.domain.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UnavailableAccessException;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {

  private final BookingRepository bookingRepository;
  private final ItemRepository itemRepository;
  private final UserRepository userRepository;

  @Override
  public BookingFullDto create(BookingDto bookingDto) {
    Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new EntityNotFoundException("Предмет не найден"));

    if (!item.getAvailable()) {
      throw new UnavailableAccessException("Нельзя взять недоступную вещь");
    }

    User user = userRepository.findById(bookingDto.getBooker()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

    if (user.getId().equals(item.getOwner())) {
      throw new EntityNotFoundException("Нельзя арендовать у самого себя");
    }

    if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
      throw new UnavailableAccessException("Дата начала аренды не может быть позже окончания");
    }

    if (bookingDto.getStart().equals(bookingDto.getEnd())) {
      throw new UnavailableAccessException("Дата начала и окончания аренды не могут быть одинаковы");
    }

    Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingDto, item, user));

    log.info("{} аренда создана", booking);

    return BookingMapper.toFullDto(booking);
  }

  @Override
  public BookingFullDto approve(Long id, Boolean approved, Long userId) {
    Booking booking = bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Аренда не найден"));

    if (!Objects.equals(booking.getItem().getOwner(), userId)) {
      throw new EntityNotFoundException("Только владелец может подвердить аренду");
    }

    if (approved && booking.getStatus().equals(Status.APPROVED)) {
      throw new UnavailableAccessException("Уже подтверждено");
    }

    booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);

    log.info("{} аренда подтверждена", booking);
    return BookingMapper.toFullDto(booking);
  }

  @Transactional(readOnly = true)
  @Override
  public BookingFullDto findById(Long id, Long userId) {
    Booking booking = bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Аренда не найдена"));

    if (!Objects.equals(booking.getBooker().getId(), userId) && !Objects.equals(booking.getItem().getOwner(), userId)) {
      throw new EntityNotFoundException("Только арендатор и арендуемый может посмотреть аренду");
    }

    log.info("Получение аренды: {}", booking);
    return BookingMapper.toFullDto(booking);
  }

  @Transactional(readOnly = true)
  @Override
  public List<BookingFullDto> findAllByState(State state, Long userId, int from, int size) {
    PageRequest page = PageRequest.of(from / size, size);

    User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

    List<Booking> bookings;

    switch (state) {
      case ALL:
        bookings = bookingRepository.findAllByBookerOrderByStartDesc(page, user);
        break;
      case PAST:
        bookings = bookingRepository.findAllByBookerAndEndBeforeOrderByStartDesc(page, user, LocalDateTime.now().truncatedTo(ChronoUnit.NANOS));
        break;
      case FUTURE:
        bookings = bookingRepository.findAllByBookerAndStartAfterOrderByEndDesc(page, user, LocalDateTime.now().truncatedTo(ChronoUnit.NANOS));
        break;
      case WAITING:
        bookings = bookingRepository.findAllByBookerAndStatusOrderByStartDesc(page, user, Status.WAITING);
        break;
      case REJECTED:
        bookings = bookingRepository.findAllByBookerAndStatusOrderByStartDesc(page, user, Status.REJECTED);
        break;
      case CURRENT:
        LocalDateTime dateTime = LocalDateTime.now();

        bookings = bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(page, user, dateTime, dateTime);
        break;

      default:
        bookings = new ArrayList<>();
    }

    log.info("Получение аренд по состоянию для {}, для пользователя {}", state, user);

    return bookings.stream().map(BookingMapper::toFullDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  @Override
  public List<BookingFullDto> findAllByStateForOwner(State state, Long userId, int from, int size) {
    PageRequest page = PageRequest.of(from / size, size);

    User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

    List<Booking> bookings;

    switch (state) {
      case ALL:
        bookings = bookingRepository.findAllByItemOwnerOrderByStartDesc(page, user.getId());
        break;
      case PAST:
        bookings = bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartDesc(page, user.getId(), LocalDateTime.now().truncatedTo(ChronoUnit.NANOS));
        break;
      case FUTURE:
        bookings = bookingRepository.findAllByItemOwnerAndStartAfterOrderByStartDesc(page, user.getId(), LocalDateTime.now().truncatedTo(ChronoUnit.NANOS));
        break;
      case WAITING:
        bookings = bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(page, user.getId(), Status.WAITING);
        break;
      case REJECTED:
        bookings = bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(page, user.getId(), Status.REJECTED);
        break;
      case CURRENT: {
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.NANOS);

        bookings = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(page, user.getId(), dateTime, dateTime);
        break;
      }

      default:
        bookings = new ArrayList<>();
    }

    log.info("Получение аренд по состоянию для {}, для владельца {}", state, user);
    return bookings.stream().map(BookingMapper::toFullDto).collect(Collectors.toList());
  }
}
