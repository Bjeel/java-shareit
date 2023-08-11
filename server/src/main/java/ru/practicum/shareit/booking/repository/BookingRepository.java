package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
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

  List<Booking> findAllByBookerOrderByStartDesc(Pageable pageable, User user);

  List<Booking> findAllByBookerAndStartAfterOrderByEndDesc(Pageable pageable, User user, LocalDateTime start);

  List<Booking> findAllByItemOwnerOrderByStartDesc(Pageable pageable, Long userId);

  List<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(Pageable pageable, Long userId, LocalDateTime start);

  List<Booking> findAllByItem(Item item);

  List<Booking> findAllByBookerAndStatusOrderByStartDesc(Pageable pageable, User user, Status status);

  List<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(Pageable pageable, User user, LocalDateTime dateTime, LocalDateTime dateTime1);

  List<Booking> findAllByBookerAndEndBeforeOrderByStartDesc(Pageable pageable, User user, LocalDateTime now);

  List<Booking> findAllByItemOwnerAndStatusOrderByStartDesc(Pageable pageable, Long id, Status status);

  List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(Pageable pageable, Long id, LocalDateTime now);

  List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(Pageable pageable, Long id, LocalDateTime dateTime, LocalDateTime dateTime1);

  Optional<Booking> findTopByItemAndBookerAndStartIsBeforeOrderByStartDesc(Item item, User user, LocalDateTime now);
}
