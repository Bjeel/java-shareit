package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.domain.UserDto;
import ru.practicum.shareit.user.domain.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDto create(UserDto user) {
    User createdUser = userRepository.save(UserMapper.toUser(user));

    log.info("Пользователь {} создан", user);

    return UserMapper.toUserDto(createdUser);
  }

  @Override
  public UserDto finOne(Long userId) {
    Optional<User> user = userRepository.findById(userId);

    if (user.isPresent()) {
      log.info("Пользователь {} найден", userId);

      return UserMapper.toUserDto(user.get());
    }

    throw new EntityNotFoundException("Пользователь не найден");
  }

  @Override
  public List<UserDto> findAll() {
    List<User> users = userRepository.findAll();

    log.info("Загрузка всех пользоватлей");

    return users
      .stream()
      .map(UserMapper::toUserDto)
      .collect(Collectors.toList());
  }

  @Override
  public UserDto update(UserDto user) {
    Optional<User> optionalUser = userRepository.findById(user.getId());

    if (optionalUser.isPresent()) {
      User gettedUser = optionalUser.get();

      if (user.getEmail() != null) {
        gettedUser.setEmail(user.getEmail());
      }

      if (user.getName() != null) {
        gettedUser.setName(user.getName());
      }

      User updatedUser = userRepository.save(gettedUser);

      log.info("Пользователь {} обновлен", user);

      return UserMapper.toUserDto(updatedUser);
    }

    throw new EntityNotFoundException("Пользователь для обновления не найден");
  }

  @Override
  public void delete(Long userId) {
    userRepository.deleteById(userId);

    log.info("Пользователь {} удален", userId);
  }
}
