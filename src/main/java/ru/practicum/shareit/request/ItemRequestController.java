package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.consts.Headers;
import ru.practicum.shareit.request.domain.ItemRequestDto;
import ru.practicum.shareit.request.domain.ItemRequestMarker;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
  private final ItemRequestService itemRequestService;


  @Validated({ItemRequestMarker.OnCreate.class})
  @PostMapping
  public ItemRequestDto crete(@NotNull @RequestBody ItemRequestDto itemRequestDto, @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    itemRequestDto.setRequester(userId);
    itemRequestDto.setCreated(LocalDateTime.now());

    return itemRequestService.create(itemRequestDto);
  }

  @GetMapping
  public List<ItemRequestDto> findAllByRequester(@NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    return itemRequestService.findAllByRequester(userId);
  }

  @GetMapping("/all")
  public List<ItemRequestDto> findAll(@NotNull @RequestHeader(Headers.USER_ID) Long userId,
                                      @RequestParam(defaultValue = "0", required = false, name = "from") int from,
                                      @RequestParam(defaultValue = "10", required = false, name = "size") int size
  ) {
    return itemRequestService.findALlPageable(userId, from, size);
  }

  @GetMapping("/{id}")
  public ItemRequestDto findById(@NotNull @RequestHeader(Headers.USER_ID) Long userId, @NotNull @PathVariable Long id
  ) {
    return itemRequestService.finById(userId, id);
  }
}
