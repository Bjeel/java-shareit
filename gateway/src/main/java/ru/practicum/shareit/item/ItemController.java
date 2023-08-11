package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.comments.domain.CommentDto;
import ru.practicum.shareit.consts.Headers;
import ru.practicum.shareit.item.domain.ItemDto;
import ru.practicum.shareit.item.domain.ItemMarker;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
	private final ItemClient itemClient;

  @Validated({ItemMarker.OnCreate.class})
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public ResponseEntity<Object> create(@Valid @RequestBody ItemDto item, @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    item.setOwner(userId);

    return itemClient.create(item);
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/{id}/comment")
  public ResponseEntity<Object> addComment(@Valid @RequestBody CommentDto commentDto,
                                  @PathVariable Long id,
                                  @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    commentDto.setAuthorId(userId);
    commentDto.setItemId(id);

    return itemClient.addComment(commentDto);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{itemId}")
  public ResponseEntity<Object> findOne(@NotNull @RequestHeader(Headers.USER_ID) Long userId, @PathVariable Long itemId) {
    return itemClient.findOne(itemId, userId);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public ResponseEntity<Object> findAll(@NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    return itemClient.findAll(userId);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/search")
  public ResponseEntity<Object> search(@NotNull @RequestParam String text) {
    return itemClient.search(text);
  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/{itemId}")
  public ResponseEntity<Object> update(@PathVariable Long itemId,
                        @RequestBody ItemDto item,
                        @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    item.setId(itemId);
    item.setOwner(userId);

    return itemClient.update(item);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{itemId}")
  public void delete(@PathVariable Long itemId) {
    itemClient.delete(itemId);
  }
}
