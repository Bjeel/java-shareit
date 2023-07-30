package ru.practicum.shareit.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.comments.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
