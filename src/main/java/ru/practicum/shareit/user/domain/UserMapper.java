package ru.practicum.shareit.user.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.exception.EntityNotFoundException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    User targetUser = new User();
    targetUser.setId(user.getId());
    targetUser.setEmail(user.getEmail());
    targetUser.setName(user.getName());


    return targetUser;
  }
}
