package ru.practicum.shareit.comments.domain;

public class CommentMapper {
  public static CommentNewDto toCommentNewDto(Comment comment) {
    return CommentNewDto
      .builder()
      .id(comment.getId())
      .authorName(comment.getAuthor().getName())
      .text(comment.getText())
      .build();
  }
}
