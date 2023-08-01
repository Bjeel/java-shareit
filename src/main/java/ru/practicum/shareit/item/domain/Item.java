package ru.practicum.shareit.item.domain;

import lombok.Data;
import ru.practicum.shareit.comments.domain.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "items")
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

  private Long request;

  @OneToMany
  private List<Comment> comments;
}
