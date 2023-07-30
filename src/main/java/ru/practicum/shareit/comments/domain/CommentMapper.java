package ru.practicum.shareit.comments.domain;

import java.time.LocalDateTime;

public class CommentMapper {
  public static CommentNewDto toCommentNewDto(Comment comment) {
    return CommentNewDto
      .builder()
      .id(comment.getId())
      .authorName(comment.getAuthor().getName())
      .text(comment.getText())
      .created(comment.getCreated())
      .build();
  }

  public static Comment toComment(CommentDto commentDto) {
    Comment comment = new Comment();

    comment.setText(commentDto.getText());
    comment.setCreated(LocalDateTime.now());

    return comment;
  }
}
