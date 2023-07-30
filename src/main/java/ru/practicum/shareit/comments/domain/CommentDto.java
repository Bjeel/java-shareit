package ru.practicum.shareit.comments.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class CommentDto {
  private Long id;

  private Long itemId;

  @NotNull
  @Size(max = 1500, min = 3)
  private String text;

  private Long authorId;
}
