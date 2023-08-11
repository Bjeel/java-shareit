package ru.practicum.shareit.request.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.domain.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Validated
@Builder
public class ItemRequestDto {
  private Long id;

  @NotBlank(groups = ItemRequestMarker.OnCreate.class)
  private String description;

  private Long requester;

  private LocalDateTime created;

  private List<ItemDto> items;
}
