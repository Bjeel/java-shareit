package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.booking.domain.BookingDto;
import ru.practicum.shareit.booking.domain.BookingMapper;
import ru.practicum.shareit.booking.domain.BookingNewDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UnavailableItemException;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

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
  public BookingNewDto create(BookingDto BookingDto) {
    Optional<Item> optionalItem = itemRepository.findById(BookingDto.getItemId());

    if (optionalItem.isEmpty()) {
      throw new EntityNotFoundException("Невозможно создать заявку к отсутствующей вещи");
    }

    if (!optionalItem.get().getAvailable()) {
      throw new UnavailableItemException("Нельзя взять недоступную вещь");
    }

    Optional<User> optionalUser = userRepository.findById(BookingDto.getBooker());

    if (optionalUser.isEmpty()) {
      throw new EntityNotFoundException("Невозможно создать заявку для не существющего пользователя");
    }

    if (BookingDto.getStart() == null || BookingDto.getEnd() == null) {
      throw new UnavailableItemException("Дата начала или окончания аренды не могут быть пустыми");
    }

    if (BookingDto.getStart().isAfter(BookingDto.getEnd())) {
      throw new UnavailableItemException("Дата начала аренды не может быть позже окончания");
    }

    if (BookingDto.getStart().equals(BookingDto.getEnd())) {
      throw new UnavailableItemException("Дата начала и окончания аренды не могут быть одинаковы");
    }

    Booking booking = bookingRepository.save(BookingMapper.toBooking(BookingDto, optionalItem.get(), optionalUser.get()));

    return BookingMapper.toDto(booking, optionalItem.get());
  }
}
