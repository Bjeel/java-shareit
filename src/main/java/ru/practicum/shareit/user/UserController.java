package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.domain.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findOne(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.finOne(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> update(@PathVariable Long userId, @Valid @RequestBody UserDto user) {
        user.setId(userId);

        return ResponseEntity.ok(userService.update(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.delete(userId));
    }
}
