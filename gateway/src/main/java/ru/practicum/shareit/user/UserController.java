package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMarker;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
	private final UserClient userClient;

  @PostMapping
  public ResponseEntity<Object> create(@Valid @RequestBody UserDto user) {
    log.info("Create user = {}", user);
    return userClient.create(user);
  }

	@GetMapping("/{userId}")
	public ResponseEntity<Object> get(@PathVariable Long userId) {
		log.info("Get userId={}", userId);
		return userClient.finOne(userId);
	}

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public ResponseEntity<Object> findAll() {
    return userClient.findAll();
  }

  @Validated({UserMarker.OnUpdate.class})
  @PatchMapping("/{userId}")
  public ResponseEntity<Object> update(@PathVariable Long userId, @Valid @RequestBody UserDto user) {
    user.setId(userId);

    return userClient.update(user);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{userId}")
  public void delete(@PathVariable Long userId) {
    userClient.delete(userId);
  }
}
