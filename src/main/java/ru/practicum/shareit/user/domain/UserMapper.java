package ru.practicum.shareit.user.domain;

import ru.practicum.shareit.exception.EntityNotFoundException;

public class UserMapper {

  public static UserDto toUserDto(User user) {
    if (user == null) {
      throw new EntityNotFoundException("Пользователь не может быть null");
    }

    return UserDto
      .builder()
      .id(user.getId())
      .name(user.getName())
      .email(user.getEmail())
      .build();
  }

  public static User toUser(UserDto user) {
    if (user == null) {
      throw new EntityNotFoundException("Пользователь не может быть null");
    }

    return User
      .builder()
      .id(user.getId())
      .name(user.getName())
      .email(user.getEmail())
      .build();
  }
}
