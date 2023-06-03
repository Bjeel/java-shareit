package ru.practicum.shareit.request.domain;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.domain.User;

import java.time.LocalDate;

@Data
@Builder
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private LocalDate created;
}
