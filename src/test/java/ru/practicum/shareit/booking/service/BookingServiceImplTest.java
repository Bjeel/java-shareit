package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
  @Mock
  private BookingRepository bookingRepository;

  @Mock
  private ItemRepository itemRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private BookingServiceImpl bookingService;

  User user = new User();
  Item item = new Item();
  Booking booking = new Booking();
  BookingFullDto bookingFullDto;
  BookingDto bookingDto;

  @BeforeEach
  void setUp() {
    bookingFullDto = BookingFullDto
      .builder()
      .id(1L)
      .booker(null)
      .item(null)
      .end(LocalDateTime.parse("2025-08-04T13:26:59"))
      .start(LocalDateTime.parse("2025-08-03T13:26:59"))
      .status(Status.WAITING)
      .build();

    bookingDto = BookingDto
      .builder()
      .id(1L)
      .booker(1L)
      .itemId(1L)
      .end(LocalDateTime.parse("2025-08-04T13:26:59"))
      .start(LocalDateTime.parse("2025-08-03T13:26:59"))
      .build();

    user.setName("John Doe");
    user.setEmail("mail@mail.ru");
    user.setId(1L);

    item.setId(1L);
    item.setAvailable(true);
    item.setOwner(2L);

    booking.setItem(item);
    booking.setId(1L);
    booking.setBooker(user);
  }

  @Test
  void saveBookingWithNotFoundItem() {
    when(itemRepository.findById(1L)).thenThrow(new EntityNotFoundException("Предмет не найден"));

    assertThrows(
      EntityNotFoundException.class,
      () -> bookingService.create(bookingDto));

    verify(itemRepository).findById(1L);
  }

  @Test
  void saveBookingWithWithNotAvailableItem() {
    item.setAvailable(false);

    when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));

    assertThrows(
      UnavailableAccessException.class,
      () -> bookingService.create(bookingDto));

    verify(itemRepository).findById(1L);
  }

  @Test
  void saveBookingWithNotFoundUser() {
    when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
    when(userRepository.findById(1L)).thenThrow(new EntityNotFoundException("Пользователь не найден"));

    assertThrows(
      EntityNotFoundException.class,
      () -> bookingService.create(bookingDto));

    verify(userRepository).findById(1L);
    verify(itemRepository).findById(1L);
  }

  @Test
  void saveBookingOwnItem() {
    item.setOwner(1L);

    when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
    when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

    assertThrows(
      EntityNotFoundException.class,
      () -> bookingService.create(bookingDto));

    verify(userRepository).findById(1L);
    verify(itemRepository).findById(1L);
  }

  @Test
  void saveBookingWithStartAfterEnd() {
    bookingDto.setStart(LocalDateTime.parse("2025-08-04T13:26:59"));
    bookingDto.setEnd(LocalDateTime.parse("2025-08-03T13:26:59"));

    when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
    when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

    assertThrows(
      UnavailableAccessException.class,
      () -> bookingService.create(bookingDto));

    verify(userRepository).findById(1L);
    verify(itemRepository).findById(1L);
  }

  @Test
  void saveBookingWithStartEqEnd() {
    bookingDto.setStart(LocalDateTime.parse("2025-08-04T13:26:59"));
    bookingDto.setEnd(LocalDateTime.parse("2025-08-04T13:26:59"));

    when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
    when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

    assertThrows(
      UnavailableAccessException.class,
      () -> bookingService.create(bookingDto));

    verify(userRepository).findById(1L);
    verify(itemRepository).findById(1L);
  }

  @Test
  void saveBooking() {
    booking = BookingMapper.toBooking(bookingDto, item, user);

    when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
    when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
    when(bookingRepository.save(booking)).thenReturn(booking);

    BookingFullDto savedBooking = bookingService.create(bookingDto);

    assertThat(savedBooking.getId(), notNullValue());
    assertThat(savedBooking.getItem(), notNullValue());
    assertThat(savedBooking.getBooker(), notNullValue());
    assertThat(savedBooking.getStatus(), is(Status.WAITING));
    assertThat(savedBooking.getStart(), is(bookingDto.getStart()));
    assertThat(savedBooking.getEnd(), is(bookingDto.getEnd()));

    verify(userRepository).findById(1L);
    verify(itemRepository).findById(1L);
    verify(bookingRepository).save(booking);
  }

  @Test
  void approveBookingWithNotFoundBooking() {
    when(bookingRepository.findById(1L)).thenThrow(new EntityNotFoundException("Аренда не найден"));

    assertThrows(
      EntityNotFoundException.class,
      () -> bookingService.approve(1L, true, 1L));

    verify(bookingRepository).findById(1L);
  }

  @Test
  void approveAlienBooking() {
    booking.setItem(item);

    when(bookingRepository.findById(1L)).thenReturn(Optional.ofNullable(booking));

    assertThrows(
      EntityNotFoundException.class,
      () -> bookingService.approve(1L, true, 1L));

    verify(bookingRepository).findById(1L);
  }

  @Test
  void approveBookingHasApproved() {
    booking.setItem(item);
    booking.setStatus(Status.APPROVED);

    when(bookingRepository.findById(1L)).thenReturn(Optional.ofNullable(booking));

    assertThrows(
      UnavailableAccessException.class,
      () -> bookingService.approve(1L, true, 2L));

    verify(bookingRepository).findById(1L);
  }

  @Test
  void rejectHasApprovedBooking() {
    booking.setStatus(Status.APPROVED);

    when(bookingRepository.findById(1L)).thenReturn(Optional.ofNullable(booking));

    bookingFullDto = bookingService.approve(1L, false, 2L);

    assertThat(bookingFullDto.getId(), notNullValue());
    assertThat(bookingFullDto.getItem(), notNullValue());
    assertThat(bookingFullDto.getBooker(), notNullValue());
    assertThat(bookingFullDto.getStatus(), is(Status.REJECTED));
    assertThat(bookingFullDto.getStart(), is(bookingFullDto.getStart()));
    assertThat(bookingFullDto.getEnd(), is(bookingFullDto.getEnd()));

    verify(bookingRepository).findById(1L);
  }

  @Test
  void approveBooking() {
    booking.setStatus(Status.WAITING);

    when(bookingRepository.findById(1L)).thenReturn(Optional.ofNullable(booking));

    bookingFullDto = bookingService.approve(1L, true, 2L);

    assertThat(bookingFullDto.getId(), notNullValue());
    assertThat(bookingFullDto.getItem(), notNullValue());
    assertThat(bookingFullDto.getBooker(), notNullValue());
    assertThat(bookingFullDto.getStatus(), is(Status.APPROVED));
    assertThat(bookingFullDto.getStart(), is(bookingFullDto.getStart()));
    assertThat(bookingFullDto.getEnd(), is(bookingFullDto.getEnd()));

    verify(bookingRepository).findById(1L);
  }

  @Test
  void findByIdWithWrongUser() {
    when(bookingRepository.findById(1L)).thenReturn(Optional.ofNullable(booking));

    assertThrows(
      EntityNotFoundException.class,
      () -> bookingService.findById(1L, 3L));

    verify(bookingRepository).findById(1L);
  }

  @Test
  void findById() {
    booking.setStatus(Status.WAITING);

    when(bookingRepository.findById(1L)).thenReturn(Optional.ofNullable(booking));

    bookingFullDto = bookingService.findById(1L, 2L);

    assertThat(bookingFullDto.getId(), notNullValue());
    assertThat(bookingFullDto.getItem(), notNullValue());
    assertThat(bookingFullDto.getBooker(), notNullValue());
    assertThat(bookingFullDto.getStatus(), is(Status.WAITING));
    assertThat(bookingFullDto.getStart(), is(bookingFullDto.getStart()));
    assertThat(bookingFullDto.getEnd(), is(bookingFullDto.getEnd()));

    verify(bookingRepository).findById(1L);
  }
}
