package ru.practicum.shareit.request.domain;

import lombok.Data;
import ru.practicum.shareit.item.domain.Item;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "requests")
public class ItemRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String description;

  @NotNull
  private Long requester;

  @NotNull
  private LocalDateTime created;

  @OneToMany
  @JoinColumn(name = "request_id")
  private List<Item> items;
}
