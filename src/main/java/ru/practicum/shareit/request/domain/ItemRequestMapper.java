package ru.practicum.shareit.request.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.domain.ItemMapper;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {
  public static ItemRequestDto toDto(ItemRequest itemRequest) {

    ItemRequestDto itemRequestDto = ItemRequestDto.builder()
      .id(itemRequest.getId())
      .description(itemRequest.getDescription())
      .requester(itemRequest.getRequester())
      .created(itemRequest.getCreated())
      .build();

    if (itemRequest.getItems() != null) {
      itemRequestDto.setItems(itemRequest.getItems().stream().map(ItemMapper::toItemDto).collect(Collectors.toList()));
    }

    return itemRequestDto;
  }

  public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
    ItemRequest itemRequest = new ItemRequest();

    itemRequest.setId(itemRequestDto.getId());
    itemRequest.setDescription(itemRequestDto.getDescription());
    itemRequest.setRequester(itemRequestDto.getRequester());
    itemRequest.setCreated(itemRequestDto.getCreated());

    return itemRequest;
  }
}
