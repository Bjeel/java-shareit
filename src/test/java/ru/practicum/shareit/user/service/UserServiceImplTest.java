package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.domain.UserDto;
import ru.practicum.shareit.user.domain.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserServiceImpl userService;

  UserDto userDto = UserDto
    .builder()
    .email("mail@mail.ru")
    .name("John Doe")
    .build();

  User newUser = new User();

  @BeforeEach
  void setUp() {
    userDto = UserDto
      .builder()
      .email("mail@mail.ru")
      .name("John Doe")
      .build();

    newUser.setName("John Doe");
    newUser.setEmail("mail@mail.ru");
    newUser.setId(1L);
  }

  @Test
  void saveUser() {
    when(userRepository.save(any()))
      .thenReturn(newUser);

    UserDto savedUser = userService.create(userDto);

    assertThat(savedUser.getId(), notNullValue());
    assertThat(savedUser.getName(), is(userDto.getName()));
    assertThat(savedUser.getEmail(), is(userDto.getEmail()));

    verify(userRepository).save(any());
  }

  @Test
  void findOne() {
    when(userRepository.findById(1L))
      .thenReturn(Optional.ofNullable(newUser));

    UserDto gotUser = userService.findOne(1L);

    assertThat(gotUser.getId(), notNullValue());
    assertThat(gotUser.getName(), is(userDto.getName()));
    assertThat(gotUser.getEmail(), is(userDto.getEmail()));

    verify(userRepository).findById(any());
  }

  @Test
  void findOneNotFound() {
    when(userRepository.findById(2L))
      .thenThrow(new EntityNotFoundException("Пользователь не найден"));

    assertThrows(
      EntityNotFoundException.class,
      () -> userService.findOne(2L));

    verify(userRepository).findById(any());
  }

  @Test
  void findAll() {
    when(userRepository.findAll())
      .thenReturn(List.of(newUser));

    List<UserDto> users = userService.findAll();

    assertThat(users, hasSize(1));
    assertThat(users.get(0).getId(), notNullValue());
    assertThat(users.get(0).getName(), is(userDto.getName()));
    assertThat(users.get(0).getEmail(), is(userDto.getEmail()));

    verify(userRepository).findAll();
  }

  @Test
  void update() {
    userDto.setName("Doe John");
    userDto.setId(1L);

    when(userRepository.findById(userDto.getId()))
      .thenReturn(Optional.of(UserMapper.toUser(userDto)));

    UserDto gotUser = userService.update(userDto);

    assertThat(gotUser.getId(), notNullValue());
    assertThat(gotUser.getName(), is(userDto.getName()));
    assertThat(gotUser.getEmail(), is(userDto.getEmail()));

    verify(userRepository).findById(any());
  }

  @Test
  void updateEmptyEmail() {
    userDto.setName("Doe John");
    userDto.setEmail(null);
    userDto.setId(1L);

    newUser.setName("Doe John");

    when(userRepository.findById(userDto.getId()))
      .thenReturn(Optional.of(newUser));

    UserDto gotUser = userService.update(userDto);

    assertThat(gotUser.getId(), notNullValue());
    assertThat(gotUser.getName(), is(newUser.getName()));
    assertThat(gotUser.getEmail(), is(newUser.getEmail()));

    verify(userRepository).findById(any());
  }

  @Test
  void updateEmptyName() {
    userDto.setName(null);
    userDto.setEmail("newEmail@mail.ru");
    userDto.setId(1L);

    newUser.setEmail("newEmail@mail.ru");

    when(userRepository.findById(userDto.getId()))
      .thenReturn(Optional.of(newUser));

    UserDto gotUser = userService.update(userDto);

    assertThat(gotUser.getId(), notNullValue());
    assertThat(gotUser.getName(), is(newUser.getName()));
    assertThat(gotUser.getEmail(), is(newUser.getEmail()));

    verify(userRepository).findById(any());
  }

  @Test
  void delete() {
    userRepository.deleteById(1L);

    verify(userRepository, times(1)).deleteById(1L);
  }
}
