package ru.practicum.shareit.user.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

  public static UserDto toUserDto(User user) {
    return UserDto
      .builder()
      .id(user.getId())
      .name(user.getName())
      .email(user.getEmail())
      .build();
  }

  public static User toUser(UserDto user) {
    User targetUser = new User();
    targetUser.setId(user.getId());
    targetUser.setEmail(user.getEmail());
    targetUser.setName(user.getName());


    return targetUser;
  }
}
