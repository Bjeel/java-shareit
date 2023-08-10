package ru.practicum.shareit.item.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comments.domain.Comment;
import ru.practicum.shareit.request.domain.ItemRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String name;

  @NotBlank
  private String description;

  @NotNull
  private Boolean available;

  @NotNull
  private Long owner;

  @Column(name = "request_id")
  private Long requestId;

  @OneToMany
  private List<Comment> comments;

  @OneToMany
  private List<ItemRequest> request;
}
