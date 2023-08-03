package ru.practicum.shareit.comments.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentNewDto {
  @NotNull
  private Long id;

  @NotBlank
  private String text;

  @NotBlank
  private String authorName;

  @NotNull
  private LocalDateTime created;
}
