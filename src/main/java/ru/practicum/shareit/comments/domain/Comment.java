package ru.practicum.shareit.comments.domain;

import lombok.Data;
import ru.practicum.shareit.user.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String text;

  private Long itemId;

  @OneToOne
  @JoinColumn(name = "author")
  private User author;

  private LocalDateTime created;
}
