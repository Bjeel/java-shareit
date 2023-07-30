package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
  public List<Booking> findAllByBookerOrderByStartDesc(User user);

  public List<Booking> findAllByBookerAndStartAfterOrderByStartDesc(User user, LocalDateTime start);

  List<Booking> findAllByItemOwnerOrderByStartDesc(Long userId);

  List<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(Long userId, LocalDateTime start);
}
