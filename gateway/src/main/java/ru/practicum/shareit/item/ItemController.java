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
import javax.validation.constraints.NotBlank;
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

    log.info("Create item = {}", item);
    return itemClient.create(item);
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/{itemId}/comment")
  public ResponseEntity<Object> addComment(@Valid @RequestBody CommentDto commentDto,
                                           @PathVariable Long itemId,
                                           @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    commentDto.setAuthorId(userId);
    commentDto.setItemId(itemId);

    log.info("Add comment to item = {}, comment = {}", itemId, commentDto);
    return itemClient.addComment(commentDto);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{itemId}")
  public ResponseEntity<Object> findOne(@NotNull @RequestHeader(Headers.USER_ID) Long userId, @PathVariable Long itemId) {
    log.info("Find item = {}, user = {}", itemId, userId);
    return itemClient.findOne(itemId, userId);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public ResponseEntity<Object> findAll(@NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    log.info("Find all items for user = {}", userId);
    return itemClient.findAll(userId);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/search")
  public ResponseEntity<Object> search(@RequestParam String text) {
    log.info("Search items by text = {}", text);
    return itemClient.search(text);
  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/{itemId}")
  public ResponseEntity<Object> update(@PathVariable Long itemId,
                                       @RequestBody ItemDto item,
                                       @NotNull @RequestHeader(Headers.USER_ID) Long userId) {
    item.setId(itemId);
    item.setOwner(userId);

    log.info("Update item = {} for userId = {} with next data = {}", itemId, userId, item);
    return itemClient.update(item);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{itemId}")
  public void delete(@PathVariable Long itemId) {
    log.info("Delete item = {}", itemId);
    itemClient.delete(itemId);
  }
}
