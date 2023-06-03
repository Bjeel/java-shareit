package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.item.domain.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemDto> create(@Valid @RequestBody Item item, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        item.setOwner(userId);

        return ResponseEntity.ok(itemService.create(item));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> findOne(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.finOne(itemId));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> findAll(
        @RequestHeader("X-Sharer-User-Id") Long userId,
        @RequestParam Optional<String> text
    ) {
        return ResponseEntity.ok(itemService.findAll(userId, text.orElse(null)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> findAllBySearch(
        @RequestParam Optional<String> text
    ) {
        return ResponseEntity.ok(itemService.search(text.orElse(null)));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(
        @PathVariable Long itemId,
        @RequestBody Item item,
        @NotNull @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        item.setId(itemId);
        item.setOwner(userId);

        return ResponseEntity.ok(itemService.update(item));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> delete(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.delete(itemId));
    }
}
