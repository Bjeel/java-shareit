package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.domain.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(User user);

    UserDto finOne(Long userId);

    List<UserDto> findAll();

    UserDto update(UserDto user);

    String delete(Long userId);
}
