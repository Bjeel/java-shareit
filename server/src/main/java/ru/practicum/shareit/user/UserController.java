package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.domain.UserDto;
import ru.practicum.shareit.user.domain.UserMarker;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
  private final UserService userService;

  @ResponseStatus(HttpStatus.OK)
  @PostMapping
  public UserDto create(@Valid @RequestBody UserDto user) {
    return userService.create(user);
  }


  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{userId}")
  public UserDto findOne(@PathVariable Long userId) {
    return userService.findOne(userId);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public List<UserDto> findAll() {
    return userService.findAll();
  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/{userId}")
  public UserDto update(@PathVariable Long userId, @RequestBody UserDto user) {
    user.setId(userId);

    return userService.update(user);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{userId}")
  public void delete(@PathVariable Long userId) {
    userService.delete(userId);
  }
}
