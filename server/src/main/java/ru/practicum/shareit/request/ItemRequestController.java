package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.consts.Headers;
import ru.practicum.shareit.request.domain.ItemRequestDto;
import ru.practicum.shareit.request.domain.ItemRequestMarker;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
  private final ItemRequestService itemRequestService;

  @ResponseStatus(HttpStatus.CREATED)
  @Validated({ItemRequestMarker.OnCreate.class})
  @PostMapping
  public ItemRequestDto crete(@NotNull @RequestBody ItemRequestDto itemRequestDto, @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    itemRequestDto.setRequester(userId);
    itemRequestDto.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.NANOS));

    return itemRequestService.create(itemRequestDto);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public List<ItemRequestDto> findAllByRequester(@NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    return itemRequestService.findAllByRequester(userId);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/all")
  public List<ItemRequestDto> findALlPageable(@NotNull @RequestHeader(Headers.USER_ID) Long userId,
                                              @PositiveOrZero @RequestParam(defaultValue = "0", name = "from") int from,
                                              @Positive @RequestParam(defaultValue = "10", name = "size") int size
  ) {
    return itemRequestService.findALlPageable(userId, from, size);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{id}")
  public ItemRequestDto findById(@NotNull @RequestHeader(Headers.USER_ID) Long userId, @NotNull @PathVariable Long id
  ) {
    return itemRequestService.finById(userId, id);
  }
}
