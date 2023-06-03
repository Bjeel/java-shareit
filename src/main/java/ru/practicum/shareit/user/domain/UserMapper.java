package ru.practicum.shareit.user.domain;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return UserDto
            .builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .build();
    }
}
