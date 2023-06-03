package ru.practicum.shareit.item.domain;

/**
 * TODO Sprint add-controllers.
 */
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto
            .builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .available(item.getAvailable())
            .owner(item.getOwner())
            .build();
    }
}
