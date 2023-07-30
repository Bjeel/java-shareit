package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
  List<Booking> findAllByBookerOrderByStartDesc(User user);

  List<Booking> findAllByBookerAndStartAfterOrderByStartDesc(User user, LocalDateTime start);

  List<Booking> findAllByItemOwnerOrderByStartDesc(Long userId);

  List<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(Long userId, LocalDateTime start);

  List<Booking> findAllByItem(Item item);

  Optional<Booking> findByItemAndBookerAndEndAfter(Item item, User user, LocalDateTime dateTime);

  List<Booking> findAllByBookerAndStatusOrderByStartDesc(User user, Status status);

  List<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(User user, LocalDateTime dateTime, LocalDateTime dateTime1);

  List<Booking> findAllByBookerAndEndBeforeOrderByStartDesc(User user, LocalDateTime now);

  List<Booking> findAllByItemOwnerAndStatusOrderByStartDesc(Long id, Status status);

  List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(Long id, LocalDateTime now);

  List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(Long id, LocalDateTime dateTime, LocalDateTime dateTime1);
}
