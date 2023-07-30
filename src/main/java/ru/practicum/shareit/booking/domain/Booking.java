package ru.practicum.shareit.booking.domain;

import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "start_time")
  private LocalDateTime start;

  @Column(name = "end_time")
  private LocalDateTime end;

  @ManyToOne
  @JoinColumn(name = "item_id")
  private Item item;

  @ManyToOne
  @JoinColumn(name = "booker_id")
  private User booker;

  private Status status;
}
