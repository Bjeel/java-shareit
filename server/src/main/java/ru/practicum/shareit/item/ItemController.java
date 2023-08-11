package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comments.domain.CommentDto;
import ru.practicum.shareit.comments.domain.CommentNewDto;
import ru.practicum.shareit.consts.Headers;
import ru.practicum.shareit.item.domain.ItemDto;
import ru.practicum.shareit.item.domain.ItemFullDto;
import ru.practicum.shareit.item.domain.ItemMarker;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
  private final ItemService itemService;

  @Validated({ItemMarker.OnCreate.class})
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public ItemDto create(@Valid @RequestBody ItemDto item, @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    item.setOwner(userId);

    return itemService.create(item);
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/{id}/comment")
  public CommentNewDto addComment(@Valid @RequestBody CommentDto commentDto,
                                  @PathVariable Long id,
                                  @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    commentDto.setAuthorId(userId);
    commentDto.setItemId(id);

    return itemService.addComment(commentDto);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{itemId}")
  public ItemFullDto findOne(@NotNull @RequestHeader(Headers.USER_ID) Long userId, @PathVariable Long itemId) {
    return itemService.findOne(itemId, userId);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public List<ItemFullDto> findAll(@NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    return itemService.findAll(userId);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/search")
  public List<ItemDto> search(@NotNull @RequestParam String text) {
    return itemService.search(text);
  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/{itemId}")
  public ItemDto update(@PathVariable Long itemId,
                        @RequestBody ItemDto item,
                        @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    item.setId(itemId);
    item.setOwner(userId);

    return itemService.update(item);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{itemId}")
  public void delete(@PathVariable Long itemId) {
    itemService.delete(itemId);
  }
}
