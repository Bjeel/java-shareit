package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.domain.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {
  private final ItemService itemService;

  @Autowired
  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public ItemDto create(@Valid @RequestBody ItemDto item, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
    item.setOwner(userId);

    return itemService.create(item);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{itemId}")
  public ItemDto findOne(@PathVariable Long itemId) {
    return itemService.finOne(itemId);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public List<ItemDto> findAll(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam Optional<String> text) {
    return itemService.findAll(userId, text.orElse(null));
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/search")
  public List<ItemDto> findAllBySearch(@RequestParam Optional<String> text) {
    return itemService.search(text.orElse(null));
  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/{itemId}")
  public ItemDto update(@PathVariable Long itemId, @RequestBody ItemDto item, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
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
