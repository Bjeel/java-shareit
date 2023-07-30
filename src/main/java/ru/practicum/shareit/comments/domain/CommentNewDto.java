package ru.practicum.shareit.comments.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentNewDto {
  private Long id;
  private String text;
  private String authorName;
  private LocalDateTime created;
}
