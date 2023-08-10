package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.domain.UserDto;
import ru.practicum.shareit.user.domain.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public UserDto create(UserDto user) {
    User createdUser = userRepository.save(UserMapper.toUser(user));

    log.info("Пользователь {} создан", user);

    return UserMapper.toUserDto(createdUser);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto findOne(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

    log.info("Пользователь {} найден", userId);

    return UserMapper.toUserDto(user);
  }

  @Transactional(readOnly = true)
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
    User gettedUser = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));


    if (user.getEmail() != null) {
      gettedUser.setEmail(user.getEmail());
    }

    if (user.getName() != null) {
      gettedUser.setName(user.getName());
    }

    log.info("Пользователь {} обновлен", user);

    return UserMapper.toUserDto(gettedUser);
  }

  @Override
  public void delete(Long userId) {
    userRepository.deleteById(userId);

    log.info("Пользователь {} удален", userId);
  }
}
