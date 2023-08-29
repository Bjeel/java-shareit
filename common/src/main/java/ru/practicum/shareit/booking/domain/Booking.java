package ru.practicum.shareit.booking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "start_time")
  @NotNull
  private LocalDateTime start;

  @Column(name = "end_time")
  @NotNull
  private LocalDateTime end;

  @ManyToOne
  @JoinColumn(name = "item_id")
  @NotNull
  private Item item;

  @ManyToOne
  @JoinColumn(name = "booker_id")
  @NotNull
  private User booker;

  @NotNull
  private Status status;
}
