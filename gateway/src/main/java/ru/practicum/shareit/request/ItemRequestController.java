package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.consts.Headers;
import ru.practicum.shareit.request.domain.ItemRequestDto;
import ru.practicum.shareit.request.domain.ItemRequestMarker;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
  private final ItemRequestClient itemRequestClient;

  @ResponseStatus(HttpStatus.CREATED)
  @Validated({ItemRequestMarker.OnCreate.class})
  @PostMapping
  public ResponseEntity<Object> crete(@NotNull @RequestBody ItemRequestDto itemRequestDto, @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    itemRequestDto.setRequester(userId);
    itemRequestDto.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.NANOS));

    log.info("Create request = {}", itemRequestDto);
    return itemRequestClient.create(itemRequestDto);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public ResponseEntity<Object> findAllByRequester(@NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    log.info("Find all requests for requester = {}", userId);
    return itemRequestClient.findAllByRequester(userId);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/all")
  public ResponseEntity<Object> findALlPageable(@NotNull @RequestHeader(Headers.USER_ID) Long userId,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
    log.info("Find all user = {}, from = {}, size = {}", userId, from, size);
    return itemRequestClient.findALlPageable(userId, from, size);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{requestId}")
  public ResponseEntity<Object> findById(@NotNull @RequestHeader(Headers.USER_ID) Long userId,
                                         @NotNull @PathVariable Long requestId) {
    log.info("Find request = {}, user = {}", requestId, userId);
    return itemRequestClient.finById(userId, requestId);
  }
}
