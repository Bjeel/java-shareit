package ru.practicum.shareit.comments.domain;

import lombok.Data;
import ru.practicum.shareit.user.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String text;

  @NotNull
  private Long itemId;

  @OneToOne
  @JoinColumn(name = "author")
  @NotNull
  private User author;

  @NotNull
  private LocalDateTime created;
}
