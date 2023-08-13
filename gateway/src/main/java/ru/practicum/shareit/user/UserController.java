package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.domain.UserDto;
import ru.practicum.shareit.user.domain.UserMarker;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
  private final UserClient userClient;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public ResponseEntity<Object> create(@Valid @RequestBody UserDto user) {
    log.info("Create user = {}", user);
    return userClient.create(user);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{userId}")
  public ResponseEntity<Object> get(@PathVariable Long userId) {
    log.info("Get userId={}", userId);
    return userClient.finOne(userId);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public ResponseEntity<Object> findAll() {
    log.info("Find all users");
    return userClient.findAll();
  }

  @Validated({UserMarker.OnUpdate.class})
  @PatchMapping("/{userId}")
  public ResponseEntity<Object> update(@PathVariable Long userId, @Valid @RequestBody UserDto user) {
    user.setId(userId);

    log.info("Update user = {}", user);
    return userClient.update(user);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{userId}")
  public void delete(@PathVariable Long userId) {
    log.info("Delete user = {}", userId);
    userClient.delete(userId);
  }
}
