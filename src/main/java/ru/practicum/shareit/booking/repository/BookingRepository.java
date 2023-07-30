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

  List<Booking> findAllByBookerAndStatus(User booker, Status status);

  List<Booking> findAllByItemOwnerOrderByStartDesc(Long userId);

  List<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(Long userId, LocalDateTime start);

  List<Booking> findAllByItemOwnerAndStatus(Long userId, Status status);

  List<Booking> findAllByItem(Item item);

  Optional<Booking> findByItemAndBookerAndEndAfter(Item item, User user, LocalDateTime dateTime);
}
