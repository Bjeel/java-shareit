package ru.practicum.shareit.item.domain;

import lombok.Data;
import ru.practicum.shareit.comments.domain.Comment;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "items")
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String description;

  private Boolean available;

  private Long owner;

  private Long request;

  @OneToMany
  private List<Comment> comments;
}
