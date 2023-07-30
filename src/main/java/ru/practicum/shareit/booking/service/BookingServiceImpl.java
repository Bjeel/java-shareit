package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

  BookingRepository bookingRepository;
  ItemRepository itemRepository;
  UserRepository userRepository;

  @Autowired
  public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
    this.bookingRepository = bookingRepository;
    this.itemRepository = itemRepository;
    this.userRepository = userRepository;
  }

  @Override
  public BookingFullDto create(BookingDto BookingDto) {
    Optional<Item> optionalItem = itemRepository.findById(BookingDto.getItemId());

    if (optionalItem.isEmpty()) {
      throw new EntityNotFoundException("Невозможно создать заявку к отсутствующей вещи");
    }

    if (!optionalItem.get().getAvailable()) {
      throw new UnavailableAccessException("Нельзя взять недоступную вещь");
    }

    Optional<User> optionalUser = userRepository.findById(BookingDto.getBooker());

    if (optionalUser.isEmpty()) {
      throw new EntityNotFoundException("Невозможно создать заявку для не существющего пользователя");
    }

    if (optionalUser.get().getId().equals(optionalItem.get().getId())) {
      throw new EntityNotFoundException("Нельзя арендовать у самого себя");
    }

    if (BookingDto.getStart() == null || BookingDto.getEnd() == null) {
      throw new UnavailableAccessException("Дата начала или окончания аренды не могут быть пустыми");
    }

    if (BookingDto.getStart().isAfter(BookingDto.getEnd())) {
      throw new UnavailableAccessException("Дата начала аренды не может быть позже окончания");
    }

    if (BookingDto.getStart().equals(BookingDto.getEnd())) {
      throw new UnavailableAccessException("Дата начала и окончания аренды не могут быть одинаковы");
    }

    Booking booking = bookingRepository.save(BookingMapper.toBooking(BookingDto, optionalItem.get(), optionalUser.get()));

    return BookingMapper.toFullDto(booking);
  }

  @Override
  public BookingFullDto approve(Long id, Boolean approved, Long userId) {
    Optional<Booking> optionalBooking = bookingRepository.findById(id);

    if (optionalBooking.isEmpty()) {
      throw new EntityNotFoundException("Аренда не найдена");
    }

    Booking booking = optionalBooking.get();

    if (!Objects.equals(booking.getItem().getOwner(), userId)) {
      throw new EntityNotFoundException("Только владелец может подвердить аренду");
    }

    if (approved && booking.getStatus().equals(Status.APPROVED)) {
      throw new UnavailableAccessException("Уже подтверждено");
    }

    booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);

    return BookingMapper.toFullDto(bookingRepository.save(booking));
  }

  @Override
  public BookingFullDto findById(Long id, Long userId) {
    Optional<Booking> optionalBooking = bookingRepository.findById(id);

    if (optionalBooking.isEmpty()) {
      throw new EntityNotFoundException("Аренда не найдена");
    }

    Booking booking = optionalBooking.get();

    if (!Objects.equals(booking.getBooker().getId(), userId) && !Objects.equals(booking.getItem().getOwner(), userId)) {
      throw new EntityNotFoundException("Только арендатор и арендуемый может посмотреть аренду");
    }

    return BookingMapper.toFullDto(booking);
  }

  @Override
  public List<BookingFullDto> findAllByState(String st, Long userId) {
    State state;

    try {
      state = State.valueOf(st);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Unknown state: " + st);
    }

    Optional<User> optionalUser = userRepository.findById(userId);

    if (optionalUser.isEmpty()) {
      throw new EntityNotFoundException("Пользователь не существует");
    }

    User user = optionalUser.get();
    List<Booking> bookings = new ArrayList<>();

    if (state.equals(State.ALL)) {
      bookings = bookingRepository.findAllByBookerOrderByStartDesc(user);
    }

    if (state.equals(State.FUTURE)) {
      bookings = bookingRepository.findAllByBookerAndStartAfterOrderByStartDesc(user, LocalDateTime.now());
    }

    return bookings.stream().map(BookingMapper::toFullDto).collect(Collectors.toList());
  }

  @Override
  public List<BookingFullDto> findAllByStateForOwner(String st, Long userId) {
    State state;

    try {
      state = State.valueOf(st);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Unknown state: " + st);
    }

    Optional<User> optionalUser = userRepository.findById(userId);

    if (optionalUser.isEmpty()) {
      throw new EntityNotFoundException("Пользователь не существует");
    }

    User user = optionalUser.get();
    List<Booking> bookings = new ArrayList<>();

    if (state.equals(State.ALL)) {
      bookings = bookingRepository.findAllByItemOwnerOrderByStartDesc(user.getId());
    }

    if (state.equals(State.FUTURE)) {
      bookings = bookingRepository.findAllByItemOwnerAndStartAfterOrderByStartDesc(user.getId(), LocalDateTime.now());
    }

    return bookings.stream().map(BookingMapper::toFullDto).collect(Collectors.toList());
  }
}
