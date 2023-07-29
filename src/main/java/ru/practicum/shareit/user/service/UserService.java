package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.domain.UserDto;

import java.util.List;

public interface UserService {
  UserDto create(UserDto user);


  UserDto finOne(Long userId);

  List<UserDto> findAll();

  UserDto update(UserDto user);

  void delete(Long userId);
}
