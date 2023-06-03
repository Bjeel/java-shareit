package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.domain.UserDto;
import ru.practicum.shareit.user.domain.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto create(User user) {
        User createdUser = userRepository.create(user);

        return UserMapper.toUserDto(createdUser);
    }

    @Override
    public UserDto finOne(Long userId) {
        User user = userRepository.findOne(userId);

        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();

        return users
            .stream()
            .map(UserMapper::toUserDto)
            .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto user) {
        User updatedUser = userRepository.update(user);

        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public String delete(Long userId) {
        return userRepository.delete(userId)? "Пользователь удален" : "Пользователья нельзя удалить";
    }
}
